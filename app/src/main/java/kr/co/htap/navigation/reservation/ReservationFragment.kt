package kr.co.htap.navigation.reservation

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kr.co.htap.R
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

    private lateinit var restaurant: ArrayList<StoreEntity>
    private lateinit var restaurantQuery: Query
    private var isLastRestaurant: Boolean = false
    private lateinit var laundry: ArrayList<StoreEntity>
    private lateinit var laundryQuery: Query
    private var isLastLaundry: Boolean = false

    private var currentCategory: String = "restaurant"
    private var belong: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationActivity = context as NavigationActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        belong = navigationActivity.belong
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReservationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureData()
        setUI()
    }

    private fun configureData() {
        restaurant = arrayListOf()
        restaurantQuery = query("restaurant")
        isLastRestaurant = false
        laundry = arrayListOf()
        laundryQuery = query("laundry")
        isLastLaundry = false
    }

    private fun query(category: String): Query {
        if (belong == "") {
            return db
                .collection("Reservation")
                .document("store")
                .collection(category)
                .orderBy("telephone")
                .limit(10)
        } else {
            return db
                .collection("Reservation")
                .document("store")
                .collection(category)
                .whereEqualTo("belong", belong)
                .limit(10)
        }
    }

    private fun setUI() {
        binding.restaurentButton.setOnClickListener {
            changeCategory("restaurant", restaurant, restaurantQuery)
        }

        binding.laundryButton.setOnClickListener {
            changeCategory("laundry", laundry, laundryQuery)
        }

        binding.reservationRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!binding.reservationRecyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE) {
                    binding.progressBar.visibility = View.VISIBLE

                    if (currentCategory == "restaurant") {
                        fetchStoreData(currentCategory, restaurant, isLastRestaurant, restaurantQuery)
                    } else {
                        fetchStoreData(currentCategory, laundry, isLastLaundry, laundryQuery)
                    }
                }
            }
        })

        binding.restaurentButton.performClick()
    }

    private fun setRecyclerView(storeData: ArrayList<StoreEntity>) {
        navigationActivity.runOnUiThread {
            adapter = ReservationListAdapter(storeData)
            binding.reservationRecyclerView.adapter = adapter
            binding.reservationRecyclerView.layoutManager = LinearLayoutManager(navigationActivity)

            val context = binding.reservationRecyclerView.context
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.anim_slide)
            binding.reservationRecyclerView.layoutAnimation = controller
            binding.reservationRecyclerView.scheduleLayoutAnimation()
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

    private fun changeCategory(type: String, arrayList: ArrayList<StoreEntity>, query: Query) {
        if (arrayList.size == 0) { fetchStoreData(type, arrayList, false, query) }
        setRecyclerView(arrayList)
        underlineButtonText(if (type.equals("restaurant")) binding.restaurentButton else binding.laundryButton)
        removeUnderlineFromButtonText(if (type.equals("restaurant")) binding.laundryButton else binding.restaurentButton)
        currentCategory = type
    }

    private fun fetchStoreData(type: String, arrayList: ArrayList<StoreEntity>, isLast: Boolean, query: Query) {
        if (isLast) {
            Toast.makeText(navigationActivity, "가게를 모두 불러왔습니다.", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
            return
        }

        query
            .get()
            .addOnSuccessListener { documentSnapshots ->
                val oldSize = arrayList.size

                for (document in documentSnapshots) {
                    arrayList.add(StoreEntity(
                        document.get("name").toString(),
                        document.get("category").toString(),
                        document.get("description").toString(),
                        document.get("image").toString(),
                        document.get("telephone").toString(),
                        document.get("address").toString(),
                        document.get("belong").toString(),
                        document.get("operationTime") as ArrayList<String>,
                        document.id))
                }

                if (documentSnapshots.size() == 0) {
                    Toast.makeText(navigationActivity, "가게를 모두 불러왔습니다.", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    return@addOnSuccessListener
                }

                val lastVisible = documentSnapshots.documents[documentSnapshots.size() - 1]

                if (type == "restaurant") {
                    restaurantQuery = query.startAfter(lastVisible).limit(10)
                    isLastRestaurant = documentSnapshots.size() < 10
                } else {
                    laundryQuery = query.startAfter(lastVisible).limit(10)
                    isLastLaundry = documentSnapshots.size() < 10
                }

                val newSize = arrayList.size
                adapter.notifyItemRangeInserted(oldSize, newSize - oldSize)
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching data: ", exception)
                Toast.makeText(navigationActivity, "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}