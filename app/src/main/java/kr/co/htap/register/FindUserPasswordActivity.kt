package kr.co.htap.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kr.co.htap.R
import kr.co.htap.databinding.ActivityFindUserPasswordBinding
/**
 *
 * @author 송원선
 * 비밀번호 찾기
 *
 */
class FindUserPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindUserPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindUserPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        // [확인] 버튼 클릭
        binding.btnInputEmail.setOnClickListener(object :OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                findPassword()
            }
        })

        // 뒤로가기 아이콘 클릭
        binding.ivGoback.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                finish()
            }
        })
    }

    /*
    *  비밀번호 찾기 (비밀번호 재설정)
    *  사용자가 입력한 이메일 조회 -> 존재하는 사용자라면 인증 후 비밀번호 재설정 메일 발송
    * */
    private fun findPassword(){
        // 사용자가 입력한 이메일
        val email = binding.etInputEmail.text.toString()
        Log.d("FindPassword", "확인 클릭 -> 이메일 : $email")
        if (!email.isEmpty()){
            val query: Query = firestore.collection("users")
                .whereEqualTo("email", email)
            query.get()
                .addOnSuccessListener { documents->
                    if (!documents.isEmpty){ // 사용자가 존재하는 경우 -> 인증 액티비티로 이동
                        val intent = Intent(this, FindUserPasswordAuthActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    }else{// 사용자가 존재하지 않는 경우
                        Toast.makeText(this, "해당하는 사용자 정보가 없습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}