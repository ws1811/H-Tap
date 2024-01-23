package kr.co.htap.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.co.htap.R
import kr.co.htap.databinding.ActivityLoginBinding
import kr.co.htap.navigation.NavigationActivity
import kr.co.htap.helper.isNotLoggedIn
import kr.co.htap.onboarding.OnboardingActivity


/**
 *
 * @author 송원선
 * 로그인
 *
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var registserView: TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firestore:FirebaseFirestore
    private var GOOGLE_LOGIN_CODE = 9001

    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>
    private lateinit var navigationIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        val loginButton: Button = binding.btnLogin
        val googleLoginButton = binding.ivGoogleLogin
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        setContentView(view)
        navigationIntent = Intent(this, NavigationActivity::class.java)
        navigationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

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

        // [아이디 찾기] 클릭
        binding.tvFindId.setOnClickListener {
            val intent = Intent(this, FindUserIdActivity::class.java)
            startActivity(intent)
        }
        // [비밀번호 찾기] 클릭
        binding.tvFindPassword.setOnClickListener {
            val intent = Intent(this, FindUserPasswordActivity::class.java)
            startActivity(intent)
        }
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == -1) {
                    Log.d("GoogleLogin", result.data.toString())
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    getGoogleInfo(task)
                }
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
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if (isFirstRun()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            return
        }

        if (Firebase.auth.isNotLoggedIn() == false) {
            startActivity(Intent(this, NavigationActivity::class.java))
        }
    }

    // 김기훈
    private fun isFirstRun(): Boolean {
        val pref = getSharedPreferences("hTap", 0)
        return pref.getBoolean("isFirstRun", true)
    }

    // 일반로그인
    private fun customLogin() {
        val email: String = binding.etId.text.toString().trim()
        val password: String = binding.etPassword.text.toString().trim()
        if (email == null)
            Toast.makeText(this@LoginActivity, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        else if (password == null)
            Toast.makeText(this@LoginActivity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        else {
            Log.d("Login", "Login Try -> email : $email")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@LoginActivity, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) { // 로그인 성공
                        Log.d("Login", "Logined success: $email")
                        onSignInSuccess()
                    } else { //로그인 실패
                        Log.w("Login", "Login failed : ", task.exception)
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    /**
     * 로그인에 성공한 경우 원래 액티비티로 이동하는 콜백
     * @author hoyeon
     */
    private fun onSignInSuccess() {
        // wonson's code
        val intent = Intent(this@LoginActivity, NavigationActivity::class.java)
        setResult(Activity.RESULT_OK)
        startActivity(intent)
    }

    /**
     * 로그인을 취소한 경우 원래 액티비티로 이동하는 콜백
     * @author hoyeon
     */
    private fun onSignInCanceled() {
        setResult(Activity.RESULT_CANCELED)
        Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onSignInCanceled()
        }
    }

    /**
     * 구글 로그인
     * @author 송원선
     */
    private fun googleLogin() {
        val signInIntent = googleSignInClient!!.signInIntent
//        googleLoginLauncher.launch(signInIntent)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("GoogleLogin", "signInWithCredential:success")
                    val user = auth.currentUser
                    checkIfUserExistsInFirestore(user)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GoogleLogin", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase

                val account = task.getResult(ApiException::class.java)!!
                Log.d("GoogleLogin", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

                startActivity(navigationIntent)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleLogin", "Google sign in failed", e)
            }
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val displayName = user.displayName
            val email = user.email
            val uid = user.uid

            Log.d("GoogleLogin", "UpdateUI - Display Name: $displayName")
            Log.d("GoogleLogin", "UpdateUI - Email: $email")
            Log.d("GoogleLogin", "UpdateUI - UID: $uid")

            onSignInSuccess()
        }
    }
    fun getGoogleInfo(completedTask: Task<GoogleSignInAccount>) {
        try {
            val TAG = "구글 로그인 결과"
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, account.id!!)
            Log.d(TAG, account.familyName!!)
            Log.d(TAG, account.givenName!!)
            Log.d(TAG, account.email!!)
        } catch (e: ApiException) {
            Log.w("GoogleLogin", "signInResult:failed code=" + e.statusCode)
        }
    }

    // 구글 로그인한 사용자가 FireStore 에 등록 되어있는지 체크 메소드
    private fun checkIfUserExistsInFirestore(user: FirebaseUser?) {
        if (user != null) {
            val userId = user.uid
            val usersCollection = firestore.collection("users")

            // Check if the user already exists in Firestore
            usersCollection.document(userId).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            // 이미 등록되어 있는 경우
                            Log.d("Firestore", "User already exists in Firestore")
                            updateUI(user)
                        } else {
                            // 등록돼있는 유저가 없다면 -> 등록
                            addUserToFirestore(user)
                        }
                    } else {
                        Log.e("Firestore", "Error checking user in Firestore", task.exception)
                    }
                }
        }
    }
    // FireStore 에 유저 추가
    private fun addUserToFirestore(user: FirebaseUser) {
        val userId = user.uid
        val email = user.email

        // Create user data
        val userData = hashMapOf(
            "userid" to userId,
            "email" to email,
        )

        // Add user data to Firestore
        firestore.collection("users").document(userId)
            .set(userData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firestore", "User added to Firestore successfully")
                    updateUI(user)
                } else {
                    Log.e("Firestore", "Error adding user to Firestore", task.exception)
                    updateUI(null)
                }
            }
    }
companion object {
    private const val TAG = "GoogleActivity"
    private const val RC_SIGN_IN = 9001
}
}