package kr.co.htap.navigation.location

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.co.htap.databinding.FragmentCheckLocationBinding
import java.util.Locale

/**
 *
 * @author eunku
 */
class CheckLocationFragment(var lp: LocationProvider) : DialogFragment() {
    private lateinit var binding: FragmentCheckLocationBinding
    private lateinit var adapter: LocationRecyclerViewAdapter
    private var locationProvider = lp
    private lateinit var db: FirebaseFirestore
    var branchList: ArrayList<BranchEntity> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
        db = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckLocationBinding.inflate(inflater)
        configureInitData()

        binding.createChattingRoom.setOnClickListener {
            setBranchList()
        }
        binding.sortItemByName.setOnClickListener{
            // TODO:  목록 정렬하기 
        }
        return binding.root
    }

    fun configureInitData(): ArrayList<BranchEntity> {
        db.collection("Branch").get().addOnSuccessListener { documents ->
            for (document in documents) {
                branchList.add(
                    BranchEntity(
                        document.get("name").toString(),
                        document.get("latitude") as Double,
                        document.get("longitude") as Double,
                        0.0
                    )
                )
            }
            adapter = LocationRecyclerViewAdapter(locationProvider, branchList)
            binding.locationRecycler.adapter = adapter
            binding.locationRecycler.layoutManager = LinearLayoutManager(context)
        }.addOnFailureListener { exception ->
            Log.d("test", "불러오기 실패")

        }
        return branchList
    }

    private fun setBranchList() {
        locationProvider.updateLocation()
        adapter = LocationRecyclerViewAdapter(locationProvider, branchList)
        binding.locationRecycler.adapter = adapter
        binding.locationRecycler.layoutManager = LinearLayoutManager(context)
    }
    
}