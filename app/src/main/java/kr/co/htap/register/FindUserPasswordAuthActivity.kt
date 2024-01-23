package kr.co.htap.register

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kr.co.htap.R
import kr.co.htap.databinding.ActivityFindUserPasswordAuthBinding
import java.util.concurrent.TimeUnit

/**
 *
 * @author 송원선
 * 비밀번호 찾기 - SMS 인증
 *
 */
class FindUserPasswordAuthActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFindUserPasswordAuthBinding
    private lateinit var email:String
    private lateinit var auth:FirebaseAuth
    private var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserPasswordAuthBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        var email = intent.getStringExtra("email")
        binding.etEmail.setText(email)
        binding.etEmail.isEnabled = false
        setContentView(binding.root)

        // [인증 요청] 클릭 -> 인증 번호 발송
        binding.tvAuthRequest.setOnClickListener {
            authByPhone()
        }
        // 인증번호 입력 후 [인증하기] 클릭
        binding.tvAuthCheck.setOnClickListener {
            val enterCode = binding.etEnterCode.text.toString()
            Toast.makeText(this, "클릭", Toast.LENGTH_SHORT).show()
            if(enterCode.isNotEmpty()){
                val credential = PhoneAuthProvider.getCredential(verificationId, enterCode)
                signInWithPhoneAuthCredential(credential)
            }
        }

    }

    // [인증 요청] 클릭
    private fun authByPhone() {
        var phone = binding.etPhone.text.toString()
        Log.d("FindPassword", "전화번호 : $phone")

        if(phone != null){
            binding.tvRetryAuth.visibility = View.VISIBLE // [인증번호 재발송] 보이게 표시
            binding.etEnterCode.isEnabled = true

            /* 사용자 휴대전화로 인증 코드 전송 */
            auth.setLanguageCode("kr") // 한국어 설정
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("FindPassword", "onVerificationCompleted:$credential")
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w("FindPassword", "onVerificationFailed", e)
                }
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken,) {
                    Log.d("FindPassword", "onCodeSent:$verificationId")
                    this@FindUserPasswordAuthActivity.verificationId = verificationId
                }
            }
            phone = formatPhoneNumber(phone) //  전화번호 E.164 형식 으로 포매팅
            Log.d("FindPassword", "변환된 전화번호 : $phone")
            sendVerifyNumber(this@FindUserPasswordAuthActivity, phone)

            binding.tvAuthCheck
        }
    }
    /* 사용자 휴대전화로 인증 코드 전송 */
    private fun sendVerifyNumber(context: FindUserPasswordAuthActivity, phoneNumber: String){
        auth.setLanguageCode("kr") // 한국어 설정
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("FindPassword", "onVerificationCompleted:$credential")
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("FindPassword", "onVerificationFailed", e)
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken,) {
                Log.d("FindPassword", "onCodeSent:$verificationId")
                this@FindUserPasswordAuthActivity.verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS) // 2분 시간제한
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    // 전화번호 포매팅
    private fun formatPhoneNumber(phoneNumber:String) : String {
        // 전화번호에서 숫자만 추출
        val digitsOnly = phoneNumber.replace("[^0-9]".toRegex(), "")

        // 국가 코드와 나머지 번호로 나누기
        val countryPart = "+82"
        val remainingPart = digitsOnly.substring(1)

        // 형식에 맞게 조합
        return "$countryPart ${remainingPart.substring(0, 2)}-${remainingPart.substring(2, 6)}-${remainingPart.substring(6)}"
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 인증 성공
                    Log.d("FindPassword", "signInWithCredential:success")
                    Toast.makeText(this, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    // 여기에 인증 성공 후의 로직 추가
                } else {
                    // 인증 실패
                    Log.w("FindPassword", "signInWithCredential:failure", task.exception)

                    // 여기에 인증 실패 후의 로직 추가
                    // 예: Toast 메시지를 통해 사용자에게 실패 메시지 표시
                    Toast.makeText(this, "인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}