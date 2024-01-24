package kr.co.htap.register

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kr.co.htap.databinding.ActivityLoginSuccessTestBinding
/**
 *
 * @author 송원선
 * 로그인 성공화면 테스트 페이지 (폐기 예정)
 *
 */
class LoginSuccessTest : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSuccessTestBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSuccessTestBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        val view = binding.root
        setContentView(view)

        val tvUesrInfo = binding.tvUserinfo
        val user = auth.currentUser
        if (user != null) {
            user.getIdToken(true)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val idToken = task.getResult().token
                        tvUesrInfo.setText("토큰 : $idToken")
                    }else{
                        Log.w("Login", "failed get Token : ${task.exception}")
                    }
                })
        } else {
            Toast.makeText(this, "user : null", Toast.LENGTH_LONG).show()
        }


    }
}