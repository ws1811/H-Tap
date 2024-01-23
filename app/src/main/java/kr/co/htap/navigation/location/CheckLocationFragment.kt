package kr.co.htap.navigation.location

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import kr.co.htap.R
import kr.co.htap.databinding.FragmentCheckLocationBinding
import kr.co.htap.databinding.FragmentMainBinding
import kr.co.htap.navigation.HomeViewPagerAdapter
import kr.co.htap.navigation.NavigationActivity
import kr.co.htap.navigation.reservation.BranchEntity

/**
 *
 * @author eunku
 */
class CheckLocationFragment : DialogFragment(){
    private lateinit var binding: FragmentCheckLocationBinding
    private lateinit var locationProvider: LocationProvider
    private lateinit var adapter: LocationRecyclerViewAdapter
    private lateinit var linearLayoutManager :LinearLayoutManager
    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationProvider = LocationProvider(context)
        locationProvider.requestLocation()
        linearLayoutManager = LinearLayoutManager(context)
        val branchList = arrayListOf(
            BranchEntity(1, "더현대서울", 37.525730, 126.927856),
            BranchEntity(2, "현대백화점 압구정본점", 37.527063, 127.027842),
            BranchEntity(3, "현대백화점 천호점", 37.538983, 127.123821)
        )
        adapter = LocationRecyclerViewAdapter(branchList)
    }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "onViewCreate_1")
        binding.locationRecycler.adapter= adapter
        binding.locationRecycler.layoutManager = linearLayoutManager
        binding.createChattingRoom.setOnClickListener {
            locationProvider.requestLocation()

        }

    }
}