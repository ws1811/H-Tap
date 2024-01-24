package kr.co.htap.onboarding

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kr.co.htap.R


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

        // skip 버튼 누르면 -> LogInActivity 로 이동
        tvSkip.setOnClickListener {
            // 김기훈
            val pref = getSharedPreferences("hTap", 0)
            val editor = pref.edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()
            finish()
        }
    }
}