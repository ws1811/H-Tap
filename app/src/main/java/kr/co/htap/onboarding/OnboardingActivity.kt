package kr.co.htap.onboarding

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kr.co.htap.R

/**
 *
 * @author 송원선
 * 온보딩 페이지 액티비티
 *
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var onboardingAdapter: OnboardingPagerAdapter
    private lateinit var tvSkip: TextView
    private var backPressedTime: Long = 0

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

    // 뒤로가기 버튼 클릭 -> 로그인 페이지로 가지 않고 앱 종료될 수 있게
    override fun onBackPressed() {

        if (System.currentTimeMillis() - backPressedTime < 2000) {
            super.onBackPressed()
            // 2초 이내에 뒤로가기 버튼을 두 번 누르면 어플리케이션 종료
            finishAffinity()
        } else {
            // 한 번 누르면 종료됩니다 메세지 출력
            Toast.makeText(this, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            backPressedTime = System.currentTimeMillis()
        }
    }
}