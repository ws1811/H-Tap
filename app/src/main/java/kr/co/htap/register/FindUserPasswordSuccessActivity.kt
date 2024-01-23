package kr.co.htap.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kr.co.htap.R
import kr.co.htap.databinding.ActivityFindUserPasswordSuccessBinding

/**
 *
 * @author 송원선
 * 비밀번호 찾기 성공 페이지
 *
 */
class FindUserPasswordSuccessActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFindUserPasswordSuccessBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserPasswordSuccessBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        auth.signOut() // 비밀번호 찾기가 끝난 후 로그인 되는 경우 있음 -> 여기서 로그아웃 처리
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}