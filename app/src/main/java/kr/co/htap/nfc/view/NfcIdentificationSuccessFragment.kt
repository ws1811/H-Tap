package kr.co.htap.nfc.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.htap.databinding.FragmentNfcIdentificationSuccessBinding
import kr.co.htap.helper.ViewBindingFragment
import kotlin.math.floor


/**
 * 서버에서 성공적으로 인증받은 사용자에게 표시하는 프래그먼트
 * @author 이호연
 */
class NfcIdentificationSuccessFragment :
    ViewBindingFragment<FragmentNfcIdentificationSuccessBinding>() {

    private var timer: CountDownTimer? = null;

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNfcIdentificationSuccessBinding =
        FragmentNfcIdentificationSuccessBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        timer = startCountDown()
    }

    fun startCountDown(): CountDownTimer? {
        return object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val left =
                    floor((((millisUntilFinished / 1000).toDouble())) + 1)
                        .toInt()
                binding.tvCountDown.text = String.format("%d초 뒤에 종료합니다.", left)
            }

            override fun onFinish() {
                activity?.finish()
            }
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NfcIdentificationSuccessFragment()
    }
}