package kr.co.htap.navigation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.co.htap.R
import kr.co.htap.databinding.ActivityNavigationBinding
import kr.co.htap.helper.isNotLoggedIn
import kr.co.htap.helper.requestLogin
import kr.co.htap.navigation.reservation.ReservationFragment
import kr.co.htap.onboarding.OnboardingActivity
import kr.co.htap.register.LoginActivity

/**
 *
 * @author 김기훈
 *
 */

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth

        setUI()
    }

    private fun setUI() {
        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainFragment -> setFragment("home", MainFragment())
                R.id.reservationFragment -> setFragment("reservation", ReservationFragment())
                R.id.myPageFragment -> setFragment("myPage", MyPageFragment())

            }
            true
        }
    }


    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(binding.mainFrameLayout.id, fragment, tag)
        }

        val home = manager.findFragmentByTag("home")
        val reservation = manager.findFragmentByTag("reservation")
        val myPage = manager.findFragmentByTag("myPage")

        if (home != null) {
            fragTransaction.hide(home)
        }

        if (reservation != null) {
            fragTransaction.hide(reservation)
        }

        if (myPage != null) {
            fragTransaction.hide(myPage)
        }

        if (tag == "home") {
            if (home != null) {
                fragTransaction.show(home)
            }
        } else if (tag == "reservation") {
            if (reservation != null) {
                fragTransaction.show(reservation)
            }
        } else if (tag == "myPage") {
            if (myPage != null) {
                fragTransaction.show(myPage)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }
}