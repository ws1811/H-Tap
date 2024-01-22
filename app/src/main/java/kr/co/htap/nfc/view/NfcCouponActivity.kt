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
import kr.co.htap.nfc.api.CouponAPI
import kr.co.htap.nfc.api.CouponResponse
import kr.co.htap.nfc.util.NFCUtil
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 읽은 NFC 유형이 쿠폰(/coupon])인 경우 동작하는 액티비티
 * @author 이호연
 */
class NfcCouponActivity : ViewBindingActivity<ActivityNfcEventBinding>() {

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun initViewBinding(): ActivityNfcEventBinding =
        ActivityNfcEventBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth
//        if (firebaseAuth.isNotSignedIn()) {
//            val errorMsg = "파이어베이스 로그인이 필요합니다."
//            onSignInRequired(errorMsg)
//            return
//        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "이 기기는 NFC를 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        supportFragmentManager.beginTransaction().apply {
            replace(binding.nfcFragment.id, NfcCouponFragment())
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
     * 시나리오 2: NFC를 태깅했고 서버에 요청을 보낸 상황
     */
    private fun sendCouponRequest(productID: String, callback: Callback<CouponResponse>) {
        val retrofit = Retrofit.Builder().baseUrl("https://getcoupon-7bs2chzkrq-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val apiService = retrofit.create(CouponAPI::class.java)
        // 태그 ID를 전달하는 GET 요청
        apiService.getCoupon(productID)
            .enqueue(callback)
    }

    /**
     * Cloud Functions를 이용해 쿠폰 발급을 시도합니다.
     */
    private fun parseNFC(intent: Intent) {
        NFCUtil.parseNFCContent(intent) { eventSource ->
            Log.w(this@NfcCouponActivity.toString(), eventSource.toString())
            sendCouponRequest(
                "1234", SimpleCallback(
                    onAddingCouponWasSuccessful, onRequestFailure
                )
            )
        }
    }

    /**
     * 시나리오 2-1: 요청을 성공적으로 보냈음
     * 요청에 문제 없으면 쿠폰함 프래그먼트로 이동
     * 요청에 문제가 있으면 시나리오 2-2로 이동
     */
    private val onAddingCouponWasSuccessful: OnResponse<CouponResponse> =
        OnResponse { _, response ->
            if (!response.isSuccessful) {
                Log.e("YourActivity", response.errorBody().toString())
                onAddingCouponWasNotSuccessful(response.message())
                return@OnResponse
            }

            val data: CouponResponse? = response.body()

            Log.w(this@NfcCouponActivity.toString(), data.toString())
            // 응답 데이터 처리
            Toast.makeText(
                this@NfcCouponActivity,
                "coupon : $data.toString()}",
                Toast.LENGTH_SHORT
            ).show()


            supportFragmentManager.beginTransaction().apply {
                replace(
                    binding.nfcFragment.id,
                    NfcCouponAddedFragment.newInstance("과자", "2023-10-21")
                )
                addToBackStack(null)
                commit()
            }
        }

    /**
     * 시나리오 2-2: 요청을 성공적으로 보냈으나 결과 값이 문제인 경우
     */
    private fun onAddingCouponWasNotSuccessful(errorMessage: String): OnFailure<CouponResponse> =
        OnFailure { _, _ ->
            val nfcIdentificationFailedFragment = NfcCouponFailedFragment.newInstance(errorMessage)
            supportFragmentManager.beginTransaction().apply {
                replace(binding.nfcFragment.id, nfcIdentificationFailedFragment)
                addToBackStack(null)
                commit()
            }
        }

    /**
     * 시나리오 2-3: 요청을 보내는 과정에서 오류가 발생
     */
    private val onRequestFailure: OnFailure<CouponResponse> = OnFailure { _, t ->
        supportFragmentManager.beginTransaction().apply {
            // TODO Replace with default error Message
            val nfcIdentificationFailedFragment =
                NfcCouponFailedFragment.newInstance(t?.message ?: "")
            replace(binding.nfcFragment.id, nfcIdentificationFailedFragment)
            addToBackStack(null)
            commit()
        }
    }

}

