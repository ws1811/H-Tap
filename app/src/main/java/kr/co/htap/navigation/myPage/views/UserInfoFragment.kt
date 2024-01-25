package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kr.co.htap.databinding.FragmentUserInfoBinding
import kr.co.htap.helper.ViewBindingFragment

/**
 * @author 호연
 */
class UserInfoFragment(val setLayout: (View) -> Unit = {}) :
    ViewBindingFragment<FragmentUserInfoBinding>() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentUserInfoBinding = FragmentUserInfoBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = auth.currentUser?.displayName ?: ""
        binding.tvUserName.text = String.format("%s님.", username.ifEmpty { "고객" })
        setLayout(view)
    }
}