package kr.co.htap.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kr.co.htap.R
import kr.co.htap.databinding.ActivityLoginBinding


/**
 *
 * @author 송원선
 * 로그인
 *
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var registserView : TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        val loginButton :Button = binding.btnLogin
        val googleLoginButton = binding.ivGoogleLogin
        auth = FirebaseAuth.getInstance()
        setContentView(view)

        // [회원가입] 클릭 -> 회원가입 액티비티로 이동
        registserView = binding.tvRegister
        registserView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭 (일반로그인)
        loginButton.setOnClickListener {
            customLogin()
        }

        // 구글 로그인
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // 구글 로그인 버튼 클릭
        googleLoginButton.setOnClickListener {
            googleLogin()
        }
    }

    // 일반로그인
    private fun customLogin(){
        val email : String = binding.etId.text.toString().trim()
        val password : String = binding.etPassword.text.toString().trim()
        if (email == null)
            Toast.makeText(this@LoginActivity, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        else if (password == null)
            Toast.makeText(this@LoginActivity,  "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        else {
            Log.d("Login", "Login Try -> email : $email")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@LoginActivity, OnCompleteListener<AuthResult> { task ->
                    if(task.isSuccessful) { // 로그인 성공
                        Log.d("Login", "Logined success: $email")
                        //val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        val intent = Intent(this@LoginActivity, LoginSuccessTest::class.java)
                        startActivity(intent)
                    } else { //로그인 실패
                        Log.w("Login", "Login failed : ", task.exception)
                        Toast.makeText(this@LoginActivity, task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
    // 구글 로그인
    private fun googleLogin() {
        val signInIntent = googleSignInClient!!.signInIntent
        var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == -1) {
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                getGoogleInfo(task)
            }
        }
        googleLoginLauncher.launch(signInIntent)
    }
    fun getGoogleInfo(completedTask: Task<GoogleSignInAccount>) {
        try {
            val TAG = "구글 로그인 결과"
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, account.id!!)
            Log.d(TAG, account.familyName!!)
            Log.d(TAG, account.givenName!!)
            Log.d(TAG, account.email!!)
        }
        catch (e: ApiException) {
            Log.w("Login", "signInResult:failed code=" + e.statusCode)
        }
    }
}