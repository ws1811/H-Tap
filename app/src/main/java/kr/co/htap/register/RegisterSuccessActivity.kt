package kr.co.htap.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kr.co.htap.databinding.ActivityRegisterSuccessBinding
import kr.co.htap.navigation.NavigationActivity

/**
 *
 * @author 송원선
 * 회원 가입 성공 화면
 *
 */
class RegisterSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterSuccessBinding
    private lateinit var buttonHome:Button
    private lateinit var buttonLogin:Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterSuccessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        
        
        buttonHome = binding.btnHome
        buttonLogin = binding.btnLogin
        
        // [홈으로] 버튼 -> 메인 액티비티로 이동
        buttonHome.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
        // [로그인] 버튼 -> 로그인 액티비티로 이동
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        
    }
}