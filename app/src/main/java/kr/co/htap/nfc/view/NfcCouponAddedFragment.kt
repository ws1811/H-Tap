package kr.co.htap.nfc.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.htap.databinding.FragmentNfcCouponAddedBinding
import kr.co.htap.helper.ViewBindingFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PRODUCT_NAME = "PRODUCT_NAME"
private const val VALID_UNTIL = "VALID_UNTIL"

/**
 * 쿠폰을 성공적으로 사용자 계정에 추가한 경우 표시되는 프래그먼트
 * @author 이호연
 */
class NfcCouponAddedFragment : ViewBindingFragment<FragmentNfcCouponAddedBinding>() {
    // TODO: Rename and change types of parameters
    private var productName: String? = null
    private var validUntil: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productName = it.getString(PRODUCT_NAME)
            validUntil = it.getString(VALID_UNTIL)
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNfcCouponAddedBinding {
        return FragmentNfcCouponAddedBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvProductName.text = productName ?: ""
            tvCouponValidUntil.text = validUntil ?: ""
            btnMoveToCoupon.setOnClickListener {
                val intent = Intent(activity, MyCouponActivity::class.java)
                activity?.startActivity(intent)
            }
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NfcCouponAddedFragment().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_NAME, param1)
                    putString(VALID_UNTIL, param2)
                }
            }
    }
}