package kr.co.htap.helper

import com.google.firebase.auth.FirebaseAuth

/**
 *
 * @author 이호연
 */
fun FirebaseAuth.isSignedIn(): Boolean {
    return currentUser != null
}

fun FirebaseAuth.isNotSignedIn(): Boolean {
    return !isSignedIn()
}
