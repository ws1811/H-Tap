package kr.co.htap.nfc.view

import android.os.Bundle
import kr.co.htap.R
import kr.co.htap.databinding.ActivityMyCouponBinding
import kr.co.htap.helper.ViewBindingActivity

class MyCouponActivity : ViewBindingActivity<ActivityMyCouponBinding>() {
    override fun initViewBinding(): ActivityMyCouponBinding {
        return ActivityMyCouponBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_coupon)
    }
}