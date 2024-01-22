package kr.co.htap.nfc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import kr.co.htap.databinding.FragmentNfcCouponFailedBinding
import kr.co.htap.helper.ViewBindingFragment

private const val ERROR_MESSAGE = "errorMsg"

/**
 * 쿠폰 발급 실패 시 표시하는 프래그먼트
 * @author 이호연
 */
class NfcCouponFailedFragment : ViewBindingFragment<FragmentNfcCouponFailedBinding>() {
    // TODO: Rename and change types of parameters
    private var errorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            errorMessage = it.getString(ERROR_MESSAGE)
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNfcCouponFailedBinding =
        FragmentNfcCouponFailedBinding.inflate(inflater, container, false)


    fun setErrorMessage(errorMessage: String) {
        binding.tvInfo.text = errorMessage
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param errorMsg Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NfcCouponFailedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(errorMsg: String) =
            NfcCouponFailedFragment().apply {
                arguments = Bundle().apply {
                    putString(ERROR_MESSAGE, errorMsg)
                }
            }
    }
}