package kr.co.htap.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import kr.co.htap.R
import kr.co.htap.navigation.NavigationActivity


class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var onboardingAdapter: OnboardingPagerAdapter
    private lateinit var tvSkip : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        tvSkip = findViewById(R.id.tv_skip)
        onboardingAdapter = OnboardingPagerAdapter(this)
        viewPager.adapter = onboardingAdapter

        // skip 버튼 누르면 -> NavigationActivity 로 이동
        tvSkip.setOnClickListener {
            finish()
        }
    }
}