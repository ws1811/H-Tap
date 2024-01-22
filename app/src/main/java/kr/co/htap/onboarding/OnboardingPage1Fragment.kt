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
 * 온보딩 화면 - 1번 페이지
 * H-Tap 소개
 */
class OnboardingPage1Fragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding_page_1, container, false)
    }
}