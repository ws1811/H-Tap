package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.htap.R
import kr.co.htap.databinding.FragmentCouponPointGiftBinding
import kr.co.htap.helper.ViewBindingFragment


/**
 * @author 호연
 */
class CouponPointGiftFragment(val setLayout: (View) -> Unit = {}) :
    ViewBindingFragment<FragmentCouponPointGiftBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCouponPointGiftBinding =
        FragmentCouponPointGiftBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setLayout(view)
        binding.btnCoupon.setOnClickListener {
            Log.d(CouponPointGiftFragment::class.java.toString(), "btnCoupon")
            with(requireActivity().supportFragmentManager.beginTransaction()) {
                setCustomAnimations(
                    R.anim.anim_slide_in_from_right_fade_in,
                    R.anim.anim_fade_out,
                    R.anim.anim_slide_in_from_left_fade_in,
                    R.anim.anim_fade_out
                )
                replace(R.id.mainFrameLayout, MyCouponFragment())
                addToBackStack(null)
                commit()
            }
        }
        binding.btnPoint.setOnClickListener {
            Log.d(CouponPointGiftFragment::class.java.toString(), "btnPoint")
            with(requireActivity().supportFragmentManager.beginTransaction()) {
                setCustomAnimations(
                    R.anim.anim_slide_in_from_right_fade_in,
                    R.anim.anim_fade_out,
                    R.anim.anim_slide_in_from_left_fade_in,
                    R.anim.anim_fade_out
                )
                replace(R.id.mainFrameLayout, MyPointFragment())
                addToBackStack(null)
                commit()
            }
        }
        binding.btnGift.setOnClickListener {
            Log.d(CouponPointGiftFragment::class.java.toString(), "btnGift")
            with(requireActivity().supportFragmentManager.beginTransaction()) {
                setCustomAnimations(
                    R.anim.anim_slide_in_from_right_fade_in,
                    R.anim.anim_fade_out,
                    R.anim.anim_slide_in_from_left_fade_in,
                    R.anim.anim_fade_out
                )
                replace(R.id.mainFrameLayout, MyPageReservationHistoryFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    companion object {
    }
}