package kr.co.htap.navigation.reservation

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kr.co.htap.databinding.FragmentReservationBinding
import kr.co.htap.navigation.NavigationActivity

/**
 *
 * @author 김기훈
 *
 */
class ReservationFragment : Fragment() {

    private lateinit var binding: FragmentReservationBinding
    private lateinit var navigationActivity: NavigationActivity
    private lateinit var adapter: ReservationListAdapter
    private lateinit var db: FirebaseFirestore

    private var restaurant: ArrayList<StoreEntity> = ArrayList()
    private var laundry: ArrayList<StoreEntity> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationActivity = context as NavigationActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReservationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSelectButton()
    }

    private fun setSelectButton() {
        binding.restaurentButton.setOnClickListener {
            if (restaurant.size == 0) {
                configureInitData("restaurant")
            }
            setRecyclerView(restaurant)
            underlineButtonText(binding.restaurentButton)
            removeUnderlineFromButtonText(binding.laundryButton)
        }

        binding.laundryButton.setOnClickListener {
            if (laundry.size == 0) {
                configureInitData("laundry")
            }
            setRecyclerView(laundry)
            underlineButtonText(binding.laundryButton)
            removeUnderlineFromButtonText(binding.restaurentButton)
        }

        binding.restaurentButton.performClick()
    }

    private fun setRecyclerView(storeData: ArrayList<StoreEntity>) {
        navigationActivity.runOnUiThread {
            adapter = ReservationListAdapter(storeData)
            binding.reservationRecyclerView.adapter = adapter
            binding.reservationRecyclerView.layoutManager = LinearLayoutManager(navigationActivity)
        }
    }

    private fun underlineButtonText(button: Button) {
        val spannableString = SpannableString(button.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        button.text = spannableString
    }

    private fun removeUnderlineFromButtonText(button: Button) {
        button.text = button.text.toString()
    }

    private fun configureInitData(type: String) {
        db.collection("Reservation")
            .document("store")
            .collection(type)
            .get()
            .addOnSuccessListener { documents ->
                val data = if (type == "restaurant") restaurant else laundry

                for (document in documents) {
                    data.add(StoreEntity(
                        document.get("name").toString(),
                        document.get("category").toString(),
                        document.get("description").toString(),
                        document.get("image").toString(),
                        document.get("telephone").toString(),
                        document.get("address").toString(),
                        document.get("belong").toString(),
                        document.get("operationTime") as ArrayList<String>))
                }
                binding.reservationRecyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("hhh", "Error getting documents: ", exception)
            }
    }
}