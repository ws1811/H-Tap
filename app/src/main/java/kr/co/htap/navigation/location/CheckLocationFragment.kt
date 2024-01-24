package kr.co.htap.navigation.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kr.co.htap.databinding.FragmentCheckLocationBinding
import kr.co.htap.navigation.reservation.BranchEntity
import kr.co.htap.navigation.reservation.StoreEntity
import okhttp3.Dispatcher

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

        if (branchList.size == 0){
            configureInitData()

        }

        binding.createChattingRoom.setOnClickListener {
            setBranchList()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun configureInitData(): ArrayList<BranchEntity> {
        db.collection("Branch").get().addOnSuccessListener { documents ->
            Log.d("test", " Document  : ${documents}")
            for (document in documents) {
                branchList.add(
                    BranchEntity(
                        document.get("name").toString(),
                        document.get("latitude") as Double,
                        document.get("longitude") as Double
                    )
                )
            }
            adapter = LocationRecyclerViewAdapter(locationProvider, branchList)
            binding.locationRecycler.adapter = adapter
            binding.locationRecycler.layoutManager = LinearLayoutManager(context)
            Log.d("testtest", "before_layoutManager")
        }.addOnFailureListener { exception ->
            Log.d("test", "불러오기 실패")

        }
        return branchList
    }
    private fun setBranchList(){
        locationProvider.updateLocation()
        adapter = LocationRecyclerViewAdapter(locationProvider, branchList)
        binding.locationRecycler.adapter = adapter
        binding.locationRecycler.layoutManager = LinearLayoutManager(context)
    }
}