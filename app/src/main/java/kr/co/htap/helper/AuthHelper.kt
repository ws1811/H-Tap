package kr.co.htap.helper

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kr.co.htap.register.LoginActivity

/**
 *
 * @author 이호연
 */
fun FirebaseAuth.isLoggedIn(): Boolean {
    return currentUser != null
}

/**
 *
 * @author 이호연
 */
fun FirebaseAuth.isNotLoggedIn(): Boolean {
    return !isLoggedIn()
}

/**
 * 로그인 페이지로 이동하는 메서드
 * @param onLoginSuccess 성공적으로 로그인한 경우 할 일을 정의
 * @param onLoginFailed 로그인에 실패한 경우 할 일을 정의
 * @author hoyeon
 */
fun AppCompatActivity.requestLogin(
    onLoginSuccess: OnLoginSuccess = OnLoginSuccess.getEmpty(),
    onLoginFailed: OnLoginFailed = OnLoginFailed.getEmpty()
) {
    val intent = Intent(this, LoginActivity::class.java)
    val contract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != AppCompatActivity.RESULT_OK) onLoginFailed.onLoginFailed(result)
            else onLoginSuccess.onLoginSuccess(result)

        }
    contract.launch(intent)
}

/**
 * 로그인에 성공한 경우 호출되는 콜백
 * @author hoyeon
 */
fun interface OnLoginSuccess {
    fun onLoginSuccess(result: ActivityResult)

    companion object {
        fun getEmpty(): OnLoginSuccess = OnLoginSuccess { }
    }
}

/**
 * 로그인에 실패한 경우 호출되는 콜백
 * @author hoyeon
 */
fun interface OnLoginFailed {
    fun onLoginFailed(result: ActivityResult)

    companion object {
        fun getEmpty(): OnLoginFailed = OnLoginFailed { }
    }
}
