package kr.co.htap.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.htap.R

/**
 *
 * @author 송원선
 * 온보딩 프래그먼트 2-1 (예약 페이지- 1)
 */
class OnboardingPageResrvation1:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding_page_2_1, container, false)
    }
}