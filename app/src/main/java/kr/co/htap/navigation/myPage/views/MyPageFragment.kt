package kr.co.htap.navigation.myPage.views

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.isNotLoggedIn()) {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        val user = auth.currentUser
        val userName = user?.displayName

        val top_margin = 32.dp()
        with(requireActivity().supportFragmentManager.beginTransaction()) {
            add(
                binding.scrollViewContent.id,
                UserInfoFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            add(
                binding.scrollViewContent.id,
                CouponPointGiftFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            add(
                binding.scrollViewContent.id,
                ManageMemberFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            add(
                binding.scrollViewContent.id,
                InfoHubFragment(setLayout = Layout.setMarginTop(top_margin))
            )
            commit()
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