package kr.co.htap.nfc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.htap.databinding.FragmentNfcActionFailedBinding
import kr.co.htap.helper.OnFragmentReady
import kr.co.htap.helper.ViewBindingFragment

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM = "errorMsg"
private const val ARG_PARAM1 = "loginRequired"

/**
 * 인증과정에서 문제가 발생한 경우 표시하는 프래그먼트
 * @author 이호연
 */
class NfcActionFailedFragment(private val onFragmentReady: OnFragmentReady<FragmentNfcActionFailedBinding>) :
    ViewBindingFragment<FragmentNfcActionFailedBinding>() {
    // TODO: Rename and change types of parameters
    private var errorMsg: String? = null
    private var loginRequired: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            errorMsg = it.getString(ARG_PARAM) ?: ""
            loginRequired = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNfcActionFailedBinding =
        FragmentNfcActionFailedBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentReady.onFragmentReady(binding)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param errorMsg Parameter 1.
         * @param loginRequired Parameter 2.
         * @return A new instance of fragment NfcIdentificationFailedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(onFragmentReady: OnFragmentReady<FragmentNfcActionFailedBinding>) =
            NfcActionFailedFragment(onFragmentReady)
    }

}