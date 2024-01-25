package kr.co.htap.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var buttonToMain:Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterSuccessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        

        buttonToMain = binding.btnToMain
        
        // [메인으로] 버튼 -> 로그인 액티비티로 이동
        buttonToMain.setOnClickListener (object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                val intent = Intent(v?.context, LoginActivity::class.java)
                startActivity(intent)
            }
        })
    }
}