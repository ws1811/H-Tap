package kr.co.htap.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 * @author 송원선
 * 온보딩 페이지 어댑터
 */
class OnboardingPagerAdapter(activity: FragmentActivity):FragmentStateAdapter(activity) {
    private val numPages = 4

    override fun getItemCount(): Int = numPages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingPage1Fragment()
            1 -> OnboardingPageResrvation1()
            2 -> OnboardingPageResrvation2()
            3 -> OnboardingPage3Fragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}