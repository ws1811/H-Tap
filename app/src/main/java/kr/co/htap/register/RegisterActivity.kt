package kr.co.htap.register


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.co.htap.databinding.ActivityRegisterBinding
import kotlin.system.measureTimeMillis

/**
 *
 * @author 송원선
 * 회원 가입
 * < 미구현 목록 >
 * 1. 회원 가입시 인증
 *
 */
class RegisterActivity: AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        firebaseDatabase = FirebaseDatabase.getInstance()

        val view = binding.root
        setContentView(view)
        Log.d("Register", "onCreate()")


        // 뒤로가기 버튼 리스너 등록
        binding.btnBack.setOnClickListener {
            finish()
        }
        // 메뉴 버튼 리스너 등록
        binding.btnMenu.setOnClickListener {
            TODO("메뉴 버튼 눌렀을 때 로직 구현")
        }
        // 회원 가입 버튼 리스너 등록
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    // 회원 가입
    fun register() {
        var validation = true
        val email = binding.etId.text.toString()
        val password = binding.etPassword.text.toString()
        val passwordCheck = binding.etPasswordCheck.text.toString()
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        val db = Firebase.firestore

        Log.d("Register", "Try register | email : $email")
        /* 입력 확인 */
        if (email.isEmpty()) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
            validation = false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            validation = false
        }
        if (passwordCheck.isEmpty()) {
            Toast.makeText(this, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
            validation = false
        }
        // 비밀번호 & 비밀번호 확인 일치 검사
        if (password != passwordCheck){
            Toast.makeText(this, "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            validation = false
        }

        // 모든 검사를 통과한 경우 -> 회원 가입 진행
        if (validation == true){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Register", "createUserWithEmail:success")

                        // 데이터베이스에 유저 추가
                        val userid = auth.currentUser?.uid.toString()
                        val user = hashMapOf(
                            "userid" to userid,
                            "email" to email,
                            "password" to password,
                            "name" to name,
                            "phone" to phone
                        )
                        // Firestore 에 회원 추가
                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d("FireStore", "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener{e->
                                Log.w("FireStore", "Error adding user", e)
                            }
                        // 회원가입 성공 화면으로 이동
                        val intent = Intent(this, RegisterSuccessActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Register", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

        }

    }

}