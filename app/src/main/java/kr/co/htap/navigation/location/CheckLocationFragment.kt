package kr.co.htap.navigation.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.htap.databinding.FragmentCheckLocationBinding
import kr.co.htap.navigation.reservation.BranchEntity

/**
 *
 * @author eunku
 */
class CheckLocationFragment(var lp : LocationProvider) : DialogFragment(){
    private lateinit var binding: FragmentCheckLocationBinding
    private lateinit var adapter: LocationRecyclerViewAdapter
    private var locationProvider = lp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckLocationBinding.inflate(inflater)
        binding.createChattingRoom.setOnClickListener {
            Log.d("testtest_click" , "${locationProvider.getLocation()?.latitude}----${locationProvider.getLocation()?.longitude}")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val branchList = arrayListOf(
            BranchEntity(1, "더현대서울", 37.525730, 126.927856),
            BranchEntity(2, "현대백화점 압구정본점", 37.527063, 127.027842),
            BranchEntity(3, "현대백화점 천호점", 37.538983, 127.123821)
        )
        Log.d("testtest_click_before", "${locationProvider.getLocation()?.latitude}----${locationProvider.getLocation()?.longitude}")

        adapter = LocationRecyclerViewAdapter(locationProvider, branchList)
        Log.d("testtest","before_adapter")
        binding.locationRecycler.adapter= adapter
        Log.d("testtest","before_layoutManager")
        binding.locationRecycler.layoutManager = LinearLayoutManager(context)

    }
}