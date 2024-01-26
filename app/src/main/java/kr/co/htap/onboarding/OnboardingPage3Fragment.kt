package kr.co.htap.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kr.co.htap.R

/**
 *
 * @author 송원선
 * 온보딩 마지막 페이지
 */
class OnboardingPage3Fragment:Fragment() {
    private lateinit var startBtn:Button

    private lateinit var onboardingActivity: OnboardingActivity

    // 김기훈
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardingActivity = context as OnboardingActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_page_3, container, false)
        startBtn = view.findViewById(R.id.btn_start)

        startBtn.setOnClickListener {
            // 김기훈
            val pref = onboardingActivity.getSharedPreferences("hTap", 0)
            val editor = pref.edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()
            onboardingActivity.finish()
        }
        return view
    }
}