package kr.co.htap.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.htap.R
import kr.co.htap.databinding.ActivityNavigationBinding
import kr.co.htap.navigation.myPage.views.MyPageFragment
import kr.co.htap.navigation.reservation.ReservationFragment

/**
 *
 * @author 김기훈
 *
 */

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private var recentPosition = 0
    private val fragments = listOf(
        MainFragment(),
        ReservationFragment(),
        MyPageFragment()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
    }

    private fun setUI() {
        binding.navigationView.setOnItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.mainFragment -> {
                    transaction.setCustomAnimations(
                        R.anim.anim_slide_in_from_left_fade_in,
                        R.anim.anim_fade_out
                    )
                    transaction.replace(binding.mainFrameLayout.id, fragments[0])
                    transaction.commit()
                    recentPosition = 0

                    return@setOnItemSelectedListener true
                }

                R.id.reservationFragment -> {
                    if (recentPosition < 1) {
                        transaction.setCustomAnimations(
                            R.anim.anim_slide_in_from_right_fade_in,
                            R.anim.anim_fade_out
                        )
                    } else {
                        transaction.setCustomAnimations(
                            R.anim.anim_slide_in_from_left_fade_in,
                            R.anim.anim_fade_out
                        )
                    }
                    transaction.replace(binding.mainFrameLayout.id, fragments[1])
                    transaction.commit()
                    recentPosition = 1
                    return@setOnItemSelectedListener true
                }

                R.id.myPageFragment -> {
                    transaction.setCustomAnimations(
                        R.anim.anim_slide_in_from_right_fade_in,
                        R.anim.anim_fade_out
                    )
                    transaction.replace(binding.mainFrameLayout.id, fragments[2])
                    transaction.commit()
                    recentPosition = 2
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}