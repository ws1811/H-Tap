package kr.co.htap.navigation.myPage.views

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kr.co.htap.databinding.FragmentMyPageBinding
import kr.co.htap.helper.Layout
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.helper.isNotLoggedIn
import kr.co.htap.register.LoginActivity

/**
 *
 * @author 김기훈
 * @author hoyeon(구현)
 */
class MyPageFragment : ViewBindingFragment<FragmentMyPageBinding>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMyPageBinding = FragmentMyPageBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.isNotLoggedIn()) {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        val top_margin = 32.dp()
        with(requireActivity().supportFragmentManager.beginTransaction()) {
            replace(
                binding.fragmentUserInfo.id,
                UserInfoFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            replace(
                binding.fragmentPointCouponGift.id,
                CouponPointGiftFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            replace(
                binding.fragmentMemberInfo.id,
                ManageMemberFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            replace(
                binding.fragmentInfo.id,
                InfoHubFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            commit()
            binding.btnSignOut.visibility = VISIBLE
        }

        binding.scrollView2.requestLayout()

    }


    fun Int.dp(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            requireContext().resources.displayMetrics
        ).toInt()

    }


    companion object {

    }
}