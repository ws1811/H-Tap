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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kr.co.htap.databinding.ActivityNfcEventBinding
import kr.co.htap.helper.ViewBindingActivity
import kr.co.htap.helper.isNotLoggedIn
import kr.co.htap.nfc.util.NFCUtil
import kr.co.htap.register.LoginActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * 읽은 NFC 유형이 쿠폰(/coupon])인 경우 동작하는 액티비티
 * @author 이호연
 */
class NfcCouponActivity : ViewBindingActivity<ActivityNfcEventBinding>() {

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun initViewBinding(): ActivityNfcEventBinding =
        ActivityNfcEventBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore

        addCoupon()
        pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
    }

    private fun addCoupon() {
        if (firebaseAuth.isNotLoggedIn()) {
            val errorMsg = "파이어베이스 로그인이 필요합니다."
            onSignInRequired()
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "이 기기는 NFC를 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        supportFragmentManager.beginTransaction().apply {
            val requestFragment = NfcActionRequestedFragment.newInstance {
                it.tvInfo.text = "쿠폰 발급 중입니다."
            }
            replace(binding.nfcFragment.id, requestFragment)
            commit()
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            parseNFC(intent)
        }
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
    private fun onSignInRequired() {
        startActivity(Intent(this, LoginActivity::class.java))
    }


    private fun parseNFC(intent: Intent) {
        NFCUtil.parseNFCContent(intent) { eventSource ->
            Log.w(this@NfcCouponActivity.toString(), eventSource.toString())
            if (!eventSource.addtionalData.containsKey("couponReference")) {
                onInvalidNfc()
            }

            val email = firebaseAuth.currentUser?.email ?: ""

            if (email.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                return@parseNFCContent
            }

            val couponId = eventSource.addtionalData["couponReference"]!!
            val couponPath = "/coupons/$couponId"
            val accountPath = "/users/${email}"
            println(accountPath)

            var couponData: List<String>? = null

            firestore.runTransaction { transaction ->
                val couponReference = firestore.document(couponPath)
                val accountReference = firestore.document(accountPath)

                val coupon = transaction.get(couponReference)

                if (!coupon.exists()) throw CouponException.INVALID_NFC_TAG

                val account = transaction.get(accountReference)

                val defaultTimezone = "Asia/Seoul"
                val timeZone = TimeZone.getTimeZone(defaultTimezone)
                val calendar = Calendar.getInstance(timeZone)
                calendar.add(Calendar.DAY_OF_MONTH, 10)
                val expires =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

                val usedCoupons = account.get("usedCoupons") as? ArrayList<String> ?: arrayListOf()

                if (usedCoupons.any { it.contains(couponId) }) {
                    throw CouponException.COUPON_ALREADY_USED
                }

                val coupons = account.get("coupons") as? ArrayList<String> ?: arrayListOf()

                if (!coupons.any { it.contains(couponId) }) { // 쿠폰을 갖고 있지 않은 경우
                    coupons.add("${couponId}:${expires}")

                    if (account.get("usedCoupons") == null) transaction.set(
                        accountReference,
                        mapOf("coupons" to coupons)
                    )
                    else transaction.update(accountReference, mapOf("coupons" to coupons))

                    couponData = listOf(
                        coupon.getString("name") ?: "",
                        coupon.getString("percent") ?: "",
                        expires
                    )
                } else { //쿠폰을 이미 갖고 있는 경우
                    throw CouponException.COUPON_ALREADY_EXIST
                }

            }.addOnSuccessListener {
                onCouponSuccessfullyAdded(couponData)
            }.addOnFailureListener { e ->
                println(e.localizedMessage)
                if (e is CouponException) {
                    when (e.code) {
                        CouponException.INVALID_NFC_TAG.code -> onInvalidNfc()
                        CouponException.COUPON_ALREADY_EXIST.code -> onCouponAlreadyExists()
                        CouponException.COUPON_ALREADY_USED.code -> onCouponAlreadyUsed()
                    }
                } else {
                    onOtherException(e)
                }
            }
        }
    }

    /**
     * @author 호연
     */
    class CouponException(val code: Int) : Exception() {
        companion object {
            val COUPON_ALREADY_USED = CouponException(0)
            val COUPON_ALREADY_EXIST = CouponException(1)
            val INVALID_NFC_TAG = CouponException(2)
        }
    }

    private fun onCouponSuccessfullyAdded(couponData: List<String>?) {
        try {
            supportFragmentManager.beginTransaction().apply {
                val successFragment = NfcActionSuccessFragment.newInstance(NfcActions.onCouponAdded(
                    String.format(
                        "쿠폰이 발급되었습니다.\n%s\n%s까지",
                        couponData?.get(0),
                        couponData?.get(2)
                    )
                ) { finish() })
                replace(binding.nfcFragment.id, successFragment)
                commit()
            }
        } catch (e: Exception) {
        }
    }

    private fun onOtherException(e: Exception) {
        try {
            supportFragmentManager.beginTransaction().apply {
                val failedFragment = NfcActionFailedFragment.newInstance(NfcActions.onCouponError(
                    "알 수 없는 오류가 발생했습니다. ${e.message}"
                ) { finish() })
                replace(binding.nfcFragment.id, failedFragment)
                commit()
            }
        } catch (e: Exception) {
        }
    }

    private fun onCouponAlreadyExists() {
        try {
            supportFragmentManager.beginTransaction().apply {
                val failedFragment = NfcActionFailedFragment.newInstance(NfcActions.onCouponError(
                    "쿠폰을 이미 갖고 있습니다."
                ) { finish() })
                replace(binding.nfcFragment.id, failedFragment)
                commit()
            }
        } catch (e: Exception) {
        }
    }

    private fun onCouponAlreadyUsed() {
        try {
            supportFragmentManager.beginTransaction().apply {
                val failedFragment = NfcActionFailedFragment.newInstance(NfcActions.onCouponError(
                    "쿠폰을 이미 사용하셨습니다."
                ) { finish() })
                replace(binding.nfcFragment.id, failedFragment)
                commit()
            }
        } catch (e: Exception) {
        }
    }

    private fun onInvalidNfc() {
        try {
            supportFragmentManager.beginTransaction().apply {
                val failedFragment = NfcActionFailedFragment.newInstance(NfcActions.onCouponError(
                    "쿠폰이 존재하지 않습니다."
                ) { finish() })
                replace(binding.nfcFragment.id, failedFragment)
                commit()
            }
        } catch (e: Exception) {
        }
    }


}

