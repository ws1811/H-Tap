package kr.co.htap.onboarding

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
 *
 */
class OnboardingPage3Fragment:Fragment() {
    private lateinit var startBtn:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_page_3, container, false)
        startBtn = view.findViewById(R.id.btn_start)

        startBtn.setOnClickListener {
            activity?.finish()
        }
        return view
    }
}