package kr.co.htap.navigation.reservation.Loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kr.co.htap.databinding.ActivityReservationLoadingBinding

/**
 *
 * @author 김기훈
 * @author 호연 (마이페이지에서 예약 정보 조회를 위해 addToMyReservationHistory 추가)
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
        val documentId = intent.getStringExtra("documentId") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val time = intent.getStringExtra("time") ?: ""
        val userid = Firebase.auth.currentUser?.uid

        val ref = db
            .collection("Reservation")
            .document("record")
            .collection(documentId)
            .document(date)
            .collection("time")
            .document(time)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(ref)

            if (snapshot.exists()) {
                throw FirebaseFirestoreException(
                    "Reservation already exists",
                    FirebaseFirestoreException.Code.ABORTED
                )
            } else {
                val data = hashMapOf("userid" to userid)
                transaction.set(ref, data)
                println(ref.id)
            }
        }.addOnSuccessListener {
            addToMyReservationHistory(documentId, date, time)
            binding.progressBar.visibility = android.view.View.GONE
            binding.stateText.text = "예약 완료"
            binding.checkAnimation.visibility = android.view.View.VISIBLE
            binding.checkAnimation.playAnimation()
            binding.confirmButton.visibility = android.view.View.VISIBLE
        }.addOnFailureListener { e ->
            Toast.makeText(this, "다른 분이 예약하셨습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /**
     * 내 예약 내역에 예약정보를 기록하는 메서드
     * @author 호연
     */
    private fun addToMyReservationHistory(storeId: String, date: String, time: String) {
        val email = Firebase.auth.currentUser?.email ?: ""
        if (email.isEmpty()) return
        db.document("/Reservation/store/restaurant/$storeId").get().addOnSuccessListener {
            val belong = it.getString("belong") ?: ""
            val storeName = it.getString("name") ?: ""
            val image = it.getString("image") ?: ""
            val data = hashMapOf(
                "branchName" to belong, "storeName" to storeName,
                "date" to date, "time" to time, "image" to image
            )
            db.collection("/users/$email/reservation").add(data)
        }
    }
}