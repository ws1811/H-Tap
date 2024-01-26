package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.htap.databinding.FragmentModifyPersonalInfoBinding
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.helper.isNotLoggedIn
import java.lang.Exception

/**
 * @author 호연
 */
class ModifyPersonalInfoFragment : ViewBindingFragment<FragmentModifyPersonalInfoBinding>() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentModifyPersonalInfoBinding =
        FragmentModifyPersonalInfoBinding.inflate(inflater, container, false)

    private var shouldShowEditNickname: Boolean = false
    private var shouldShowEditPassword: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        listOf(
            binding.labelEditNickname2,
            binding.editNickname,
            binding.labelPassword,
            binding.editCurrentPassword,
            binding.labelNewPassword,
            binding.editNewPassword,
            binding.labelNewPassword2,
            binding.editNewPassword2
        ).forEach { it -> it.visibility = GONE }

        binding.btnEditNickname.setOnClickListener {
            listOf(
                binding.editNickname, binding.labelEditNickname2
            ).forEach { it ->
                it.visibility = if (shouldShowEditNickname) VISIBLE else GONE
            }
            shouldShowEditNickname = !shouldShowEditNickname
        }

        binding.btnEditPassword.setOnClickListener {
            listOf(
                binding.labelPassword,
                binding.editCurrentPassword,
                binding.labelNewPassword,
                binding.editNewPassword,
                binding.labelNewPassword2,
                binding.editNewPassword2
            ).forEach { it ->
                it.visibility = if (shouldShowEditPassword) VISIBLE else GONE
            }
            shouldShowEditPassword = !shouldShowEditPassword
        }
        binding.btnSubmit.setOnClickListener {
            onUpdateNickName()
            onPasswordUpdateClicked()
        }
    }

    private fun preCheckNickNameUpdateCondition(): Boolean {
        val newNickName = binding.editNickname.text.toString()
        @Suppress("RedundantIf")
        if (!shouldShowEditNickname && newNickName.isEmpty()) return false
        return true
    }

    private fun preCheckPasswordUpdateCondition(): Boolean {
        binding.let {
            val currentPassword = it.editCurrentPassword.text.toString()
            val newPassword = it.editNewPassword.text.toString()
            val newPasswordConfirm = it.editNewPassword2.text.toString()

            if (!shouldShowEditPassword && currentPassword.isEmpty() && newPassword.isEmpty() && newPasswordConfirm.isEmpty()) return false
            if (auth.isNotLoggedIn()) return false

            if (newPassword.isEmpty()) {
                Toast.makeText(context, "새 비밀번호란이 비어있습니다.", Toast.LENGTH_SHORT).show()
                return false
            }
            if (newPasswordConfirm.isEmpty()) {
                Toast.makeText(context, "새 비밀번호 확인란이 비어있습니다.", Toast.LENGTH_SHORT).show()
                return false
            }
            if (newPassword != newPasswordConfirm) {
                Toast.makeText(context, "새 비밀번호와 새 비밀번호 확인란이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }
    }

    private fun onUpdateNickName() {
        if (!preCheckNickNameUpdateCondition()) return
        val context = requireContext()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val updateTask = doUpdateUserNickName()
                if (updateTask != null) {
                    updateTask.await()
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "성공적으로 닉네임을 변경했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "닉네임 변경에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun onPasswordUpdateClicked() {
        if (!preCheckPasswordUpdateCondition()) return
        binding.let {
            val currentPassword = it.editCurrentPassword.text.toString()
            val newPassword = it.editNewPassword.text.toString()
            val currentUser = auth.currentUser!!
            val context = requireContext()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    currentUser.reauthenticate(
                        EmailAuthProvider.getCredential(
                            auth.currentUser?.email!!,
                            currentPassword
                        )
                    ).await()//
                    doUpdatePassword(newPassword)!!.await()
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "비밀번호를 변경 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    requireActivity().supportFragmentManager.popBackStack()
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "비밀번호를 변경할 수 없습니다.${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun doUpdatePassword(newPassword: String): Task<Void>? {
        return auth.currentUser?.updatePassword(newPassword)
    }

    private fun doUpdateUserNickName(): Task<Void>? {
        val newNickName = binding.editNickname.text.toString()
        if (newNickName.isEmpty()) return null
        return auth.currentUser?.updateProfile(userProfileChangeRequest {
            displayName = newNickName
        })
    }

}