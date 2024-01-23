package kr.co.htap.navigation.reservation.Loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.integrity.p
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kr.co.htap.R
import kr.co.htap.databinding.ActivityReservationLoadingBinding
import java.util.Timer

/**
 *
 * @author 김기훈
 *
 */
class ReservationLoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationLoadingBinding

    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
    }

    override fun onStart() {
        super.onStart()

        setUI()
        reserve()
    }

    private fun setUI() {
        binding.confirmButton.setOnClickListener {
            finishAffinity()
        }
    }

    private fun reserve() {
        val storeName = intent.getStringExtra("name")?: ""
        val date = intent.getStringExtra("date")?: ""
        val time = intent.getStringExtra("time")?: ""
        val userid = Firebase.auth.currentUser?.uid

        val ref = db
            .collection("Reservation")
            .document("record")
            .collection(storeName)
            .document(date)
            .collection("time")
            .document(time)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(ref)

            if (snapshot.exists()) {
                throw FirebaseFirestoreException("Reservation already exists", FirebaseFirestoreException.Code.ABORTED)
            } else {
                val data = hashMapOf("userid" to userid)
                transaction.set(ref, data)
            }
        }.addOnSuccessListener {
            binding.progressBar.visibility = android.view.View.GONE
            binding.stateText.text = "예약 완료"
            binding.checkImage.visibility = android.view.View.VISIBLE
            binding.confirmButton.visibility = android.view.View.VISIBLE
        }.addOnFailureListener { e ->
            Toast.makeText(this, "다른 분이 예약하셨습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}