package kr.co.htap.nfc.view

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kr.co.htap.databinding.ActivityNfcEventBinding
import kr.co.htap.helper.OnFailure
import kr.co.htap.helper.OnResponse
import kr.co.htap.helper.SimpleCallback
import kr.co.htap.helper.ViewBindingActivity
import kr.co.htap.helper.isNotSignedIn
import kr.co.htap.nfc.api.IdentificationAPI
import kr.co.htap.nfc.api.IdentificationResponse
import kr.co.htap.nfc.util.NFCUtil
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 읽은 NFC 유형이 인증(/identification])인 경우 동작하는 액티비티
 * @author 이호연
 */
class NfcIdentificationActivity : ViewBindingActivity<ActivityNfcEventBinding>() {

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private lateinit var firebaseAuth: FirebaseAuth
    override fun initViewBinding(): ActivityNfcEventBinding =
        ActivityNfcEventBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = Firebase.auth


        if (!firebaseAuth.isNotSignedIn()) {
            val errorMsg = "파이어베이스 로그인이 필요합니다."
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            onSignInRequired(errorMsg)
            return
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "이 기기는 NFC를 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        supportFragmentManager.beginTransaction().apply {
            replace(binding.nfcFragment.id, NfcIdentificationFragment())
            commit()
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            parseNFC(intent)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
    }


    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            NFCUtil.parseNFCContent(intent) { }
        }
    }

    private fun parseNFC(intent: Intent) {
        NFCUtil.parseNFCContent(intent) { eventSource ->
            Log.w(this@NfcIdentificationActivity.toString(), eventSource.toString())

            sendAuthGetRequest(
                "hoyeon", SimpleCallback(
                    onIdentificationWasSuccessful, onRequestFailure
                )
            )
        }
    }

    /**
     * 시나리오 1: NFC를 태깅했지만 사용자가 로그인되어 있지 않은 경우
     * TODO: @원선님이 작성한 로그인페이지로 리다이렉션
     */
    private fun onSignInRequired(errorMsg: String) {
        val nfcIdentificationFailedFragment =
            NfcIdentificationFailedFragment.newInstance(errorMsg, true)
        supportFragmentManager.beginTransaction().apply {
            replace(binding.nfcFragment.id, nfcIdentificationFailedFragment)
            addToBackStack(null)
            commit()
        }
    }

    /**
     * 시나리오 2: NFC 태깅 후 로그인 된 상태
     */
    private fun sendAuthGetRequest(
        tagId: String, callback: Callback<IdentificationResponse>
    ) {
        val retrofit = Retrofit.Builder().baseUrl("https://nfcsignin-7bs2chzkrq-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val apiService = retrofit.create(IdentificationAPI::class.java)
        apiService.nfcSignIn(tagId).enqueue(callback) // 태그 ID를 전달하는 GET 요청
    }


    /**
     * 시나리오 2-1: 서버에 성공적으로 요청을 보냈음
     * 인증 성공시 프래그먼트 교체
     * 인증 실패시 시나리오 2-2로 이동
     */
    private val onIdentificationWasSuccessful: OnResponse<IdentificationResponse> =
        OnResponse { _, response ->
            if (!response.isSuccessful) {
                Toast.makeText(
                    this@NfcIdentificationActivity,
                    "auth : ${response.errorBody().toString()}",
                    Toast.LENGTH_SHORT
                ).show()
                onIdentificationWasNotSuccessful(response.message())
                finish()
            }
            val data: IdentificationResponse? = response.body()
            Log.w(this@NfcIdentificationActivity.toString(), data.toString())

            // 응답 데이터 처리
            Toast.makeText(
                this@NfcIdentificationActivity, "auth : ${data.toString()}", Toast.LENGTH_SHORT
            ).show()

            val identificationFragment = NfcIdentificationSuccessFragment.newInstance()
            supportFragmentManager.beginTransaction().apply {
                replace(binding.nfcFragment.id, identificationFragment)
                addToBackStack(null)
                commit()
            }

        }


    /**
     * 시나리오 2-2: 서버에 성공적으로 요청을 보냈으나 무언가 문제가 있음
     * 사용자에게 오류 메시지를 표시
     */
    private fun onIdentificationWasNotSuccessful(errorMsg: String): OnFailure<IdentificationResponse> =

        OnFailure { _, _ ->
            val nfcIdentificationFailedFragment =
                NfcIdentificationFailedFragment.newInstance(errorMsg)
            supportFragmentManager.beginTransaction().apply {
                replace(binding.nfcFragment.id, nfcIdentificationFailedFragment)
                addToBackStack(null)
                commit()
            }
        }

    /**
     * 시나리오 2-3: 서버에 요청을 보낼 때 문제가 생겼음
     * 사용자에게 오류 메시지를 표시
     */
    private val onRequestFailure: OnFailure<IdentificationResponse> = OnFailure { _, t ->
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            val nfcIdentificationFailedFragment =
                NfcIdentificationFailedFragment.newInstance(t?.message)
            replace(binding.nfcFragment.id, nfcIdentificationFailedFragment)
            addToBackStack(null)
            commit()
        }
    }

}

