package kr.co.htap.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.Query
import kr.co.htap.R
import kr.co.htap.databinding.ActivityNavigationBinding
import kr.co.htap.navigation.home.HomeFragment
import kr.co.htap.navigation.myPage.views.MyPageFragment
import kr.co.htap.navigation.reservation.ReservationFragment
import kr.co.htap.navigation.reservation.StoreEntity

/**
 *
 * @author 김기훈
 *
 */

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private var recentPosition = 0
    private val fragments = listOf(
        HomeFragment(),
        ReservationFragment(),
        MyPageFragment()
    )

    var restaurant: ArrayList<StoreEntity> = arrayListOf()
    lateinit var restaurantQuery: Query
    var isLastRestaurant: Boolean = false
    var laundry: ArrayList<StoreEntity> = arrayListOf()
    lateinit var laundryQuery: Query
    var isLastLaundry: Boolean = false
    var belong = ""
    var fetchBelong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
    }

    private fun setUI() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainFrameLayout.id, fragments[0])
        transaction.commit()

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
                    transaction.addToBackStack(null)
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