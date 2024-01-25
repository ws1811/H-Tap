package kr.co.htap.register

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.co.htap.R
import kr.co.htap.databinding.ActivityFindUserPasswordAuthBinding
import java.util.Locale
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
    private lateinit var countdownTimer:CountDownTimer
    private var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserPasswordAuthBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        email = intent.getStringExtra("email") ?: ""
        binding.etEmail.setText(email)
        binding.etEmail.isEnabled = false
        setContentView(binding.root)
        /* 타이머 설정 */
        val timerTextView: TextView = binding.tvTimer
        countdownTimer = object : CountDownTimer(120000, 1000) { // 2분(120000ms) 동안 1초(1000ms)마다 호출
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                timerTextView.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            }
            override fun onFinish() {
                timerTextView.text = "00:00" // 타이머 종료 시간
            }
        }
        // 전화번호 입력시 자동 하이픈(-) 처리
        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        // [인증 요청] 클릭 -> 인증 번호 발송
        binding.tvAuthRequest.setOnClickListener (object :OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                authByPhone()
            }
        })
        // 인증번호 입력 후 [인증하기] 클릭 -> 인증 진행
        binding.btnAuthCheck.setOnClickListener (object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                Log.d("FindPassword", "btnAuthCheck Clicked")
                val enterCode = binding.etEnterCode.text.toString()
                if(enterCode.isNotEmpty()){
                    val credential = PhoneAuthProvider.getCredential(verificationId, enterCode)
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })
        // [인증번호 재발송] 클릭
        binding.tvRetryAuth.setOnClickListener(object :OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                authByPhone()
            }
        })
        // 뒤로가기 아이콘 클릭
        binding.ivGoback.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                finish()
            }
        })
    }

    // [인증 요청] 클릭
    private fun authByPhone() {
        var phone = binding.etPhone.text.toString()
        Log.d("FindPassword", "전화번호 : $phone")
        if(phone.isEmpty()){
            Toast.makeText(this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if(phone != null){
            binding.tvAuthRequest.isEnabled = false // [인증 요청] 비활성화 & 회색으로 변경
            binding.tvAuthRequest.setTextColor(Color.rgb(128, 128, 128))
            Toast.makeText(this, "인증번호가 발송되었습니다.", Toast.LENGTH_SHORT).show()
            binding.tvRetryAuth.visibility = View.VISIBLE // [인증번호 재발송] 보이게 표시
            binding.tvTimer.visibility = View.VISIBLE // 2분 타이머 표시
            binding.etEnterCode.isEnabled = true

            // 타이머 시작
            countdownTimer?.cancel() // 기존에 동작중인 타이머가 있다면 취소
            countdownTimer.start()

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
                Log.d("FindPassword", "onCodeSent:$verificationId ")
                this@FindUserPasswordAuthActivity.verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS) // 2분 시간제한
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        Log.d("FindPassword", "phoneNumber : $phoneNumber")
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
                    // 비밀번호 재설정 메일 전송
                    Firebase.auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task->
                            if(task.isSuccessful){ // 이메일 전송 성공
                                Log.d("FindPassword", "success send passwordResetEmail")
                                val onSuccessIntent = Intent(this, FindUserPasswordSuccessActivity::class.java)
                                startActivity(onSuccessIntent)
                            }else{
                                Log.w("FindPassword", "Error sending reset email", task.exception)
                            }
                        }
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