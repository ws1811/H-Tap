package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kr.co.htap.databinding.FragmentMyPagePointBinding
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.navigation.myPage.adapter.PointViewAdapter
import kr.co.htap.navigation.myPage.model.PointHistory

/**
 * @author 호연
 */
class MyPointFragment : ViewBindingFragment<FragmentMyPagePointBinding>() {

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var gson: Gson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
        gson = Gson()
    }

    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentMyPagePointBinding = FragmentMyPagePointBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val list: MutableList<PointHistory> = mutableListOf()
        var point: Long = 0
        firestore.document("points/${auth.currentUser?.uid}").get().addOnCompleteListener {
            with(it.result) {
                if (exists()) {
                    point = getLong("point") ?: 0
                    val stringArray: List<String>? = get("history") as? List<String>
                    stringArray?.forEach { item ->
                        try {
                            list.add(gson.fromJson(item, PointHistory::class.java))
                        } catch (_: Exception) {
                        }
                    }
                }
                try {
                    requireActivity().runOnUiThread {
                        binding.pointHistory.adapter = PointViewAdapter(list)
                        binding.pointHistory.layoutManager = LinearLayoutManager(context)
                        binding.tvPoint.text = String.format("%d p", point)
                    }

                } catch (e: Exception) {

                }

            }
        }

    }

    companion object {

    }
}