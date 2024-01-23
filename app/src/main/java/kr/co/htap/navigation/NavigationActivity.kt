package kr.co.htap.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kr.co.htap.R
import kr.co.htap.databinding.ActivityNavigationBinding
import kr.co.htap.navigation.reservation.ReservationFragment

/**
 *
 * @author 김기훈
 *
 */

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        for (fragmentTag in arrayListOf("home", "reservation", "myPage")) {
            val fragment = manager.findFragmentByTag(fragmentTag)

            if (fragment != null) {
                fragTransaction.hide(fragment)
                if (fragmentTag == tag) { fragTransaction.show(fragment) }
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}