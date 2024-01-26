package kr.co.htap.nfc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.htap.databinding.FragmentNfcActionSuccessBinding
import kr.co.htap.helper.OnFragmentReady
import kr.co.htap.helper.ViewBindingFragment


/**
 * 서버에서 성공적으로 인증받은 사용자에게 표시하는 프래그먼트
 * @author 이호연
 */
class NfcActionSuccessFragment(private val onFragmentReady: OnFragmentReady<FragmentNfcActionSuccessBinding>) :
    ViewBindingFragment<FragmentNfcActionSuccessBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNfcActionSuccessBinding =
        FragmentNfcActionSuccessBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentReady.onFragmentReady(binding)
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        @JvmStatic
        fun newInstance(onFragmentReady: OnFragmentReady<FragmentNfcActionSuccessBinding>)
            = NfcActionSuccessFragment(onFragmentReady)

    }
}