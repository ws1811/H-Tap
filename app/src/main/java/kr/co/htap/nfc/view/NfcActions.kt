package kr.co.htap.nfc.view

import kr.co.htap.databinding.FragmentNfcActionFailedBinding
import kr.co.htap.databinding.FragmentNfcActionSuccessBinding
import kr.co.htap.helper.OnFragmentReady

/**
 * @author 호연
 */

object NfcActions {
    fun onCouponAdded(
        msg: String,
        btnAction: (FragmentNfcActionSuccessBinding) -> Unit = {}
    ): OnFragmentReady<FragmentNfcActionSuccessBinding> {
        return OnFragmentReady { binding ->
            binding.tvInfo.text = msg
            binding.btnDone.apply {
                text = "종료하기"
                setOnClickListener {
                    btnAction(binding)
                }
            }
        }
    }

    fun onCouponError(
        msg: String,
        btnAction: (FragmentNfcActionFailedBinding) -> Unit = {}
    ): OnFragmentReady<FragmentNfcActionFailedBinding> {
        return OnFragmentReady { binding ->
            binding.tvInfo.text = msg
            binding.btnDone.apply {
                text = "종료하기"
                setOnClickListener { btnAction(binding) }
            }
        }
    }

    fun onReservationCheckSuccess(
        msg: String,
        btnAction: (FragmentNfcActionSuccessBinding) -> Unit = { }
    ): OnFragmentReady<FragmentNfcActionSuccessBinding> {
        return OnFragmentReady { binding ->
            binding.tvInfo.text = msg
            binding.btnDone.apply {
                text = "종료"
                setOnClickListener {
                    btnAction(binding)
                }
            }
        }
    }

    fun onReservationCheckFailed(
        errorMsg: String,
        btnAction: (FragmentNfcActionFailedBinding) -> Unit = {}
    ): OnFragmentReady<FragmentNfcActionFailedBinding> {
        return OnFragmentReady { binding ->
            binding.tvInfo.text = errorMsg
            binding.btnDone.apply {
                text = "종료하기"
                setOnClickListener {
                    btnAction(binding)
                }
            }
        }
    }

}
