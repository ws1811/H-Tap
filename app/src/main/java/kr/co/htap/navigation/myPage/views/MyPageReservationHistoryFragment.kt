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
import kr.co.htap.databinding.FragmentMyPageReservationHistoryBinding
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.navigation.myPage.adapter.ReservationHistoryAdapter
import kr.co.htap.navigation.myPage.model.ReservationHistory

/**
 * @author 호연
 */
class MyPageReservationHistoryFragment :
    ViewBindingFragment<FragmentMyPageReservationHistoryBinding>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var historyAdapter: ReservationHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMyPageReservationHistoryBinding =
        FragmentMyPageReservationHistoryBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        historyAdapter = ReservationHistoryAdapter(mutableListOf())
        val userEmail = auth.currentUser?.email ?: ""
        if (userEmail.isNotEmpty()) {
            fetchReservationHistory(userEmail)
        }
        binding.reservationHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun convertDateString(date: String): String {
        val splitted = date.split("-")
        return String.format("%s년 %s월 %s일", splitted[0], splitted[1], splitted[2])
    }

    private fun convertTimeString(time: String): String {
        val splitted = time.split(":")
        return String.format("%s시 %s분", splitted[0], splitted[1])
    }

    private fun fetchReservationHistory(userEmail: String) {
        val absolutePath = String.format("/users/${userEmail}/reservation")

        firestore.collection(absolutePath).get().addOnCompleteListener {
            val histories = mutableListOf<ReservationHistory>()
            val documents = it.result.documents

            documents.forEach {
                val branchName = it.getString("branchName") ?: ""
                val storeName = it.getString("storeName") ?: ""
                var date = it.getString("date") ?: ""
                if (date.isNotEmpty()) date = convertDateString(date)
                var time = it.getString("time") ?: ""
                if (time.isNotEmpty()) time = convertTimeString(time)

                val image = it.getString("image") ?: ""
                histories.add(ReservationHistory(branchName, storeName, date, time, image))
            }
            historyAdapter.apply {
                history = histories
                try {
                    requireActivity().runOnUiThread {
                        notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

}