package kr.co.htap.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import kr.co.htap.databinding.FragmentMainBinding
import kr.co.htap.navigation.location.CheckLocationFragment
import kr.co.htap.navigation.location.HomeDTO
import kr.co.htap.navigation.location.LocationProvider
import kr.co.htap.navigation.location.LocationRecyclerViewAdapter

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navigationActivity: NavigationActivity
    private lateinit var adapter: HomeViewPagerAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var branchName :String
    var itemList = ArrayList<HomeDTO>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationActivity = context as NavigationActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()

        setFragmentResultListener("requestKey"){
                requestKey, bundle ->
            this.onPause()
            val result = bundle.getString("bundleKey")
            Log.d("test123 ", "${result}")
            branchName = result!!
            getViewByBranch(branchName)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInitView()
        var locationProvider = LocationProvider(navigationActivity)
        locationProvider.getLocation()
        binding.btFindBranch.setOnClickListener {
            val dialog = CheckLocationFragment(locationProvider)
            dialog.show(requireActivity().supportFragmentManager, "CheckLocationFragment")
        }
    }

    private fun getInitView(): ArrayList<HomeDTO> {
        itemList.clear()
        var count = 10;
        db.collection("Reservation2")
            .document("store")
            .collection("restaurant")
            .get().addOnSuccessListener { documents ->

                for (document in documents.shuffled()) {
                    itemList.add(
                        HomeDTO(
                            document.getString("name")!!,
                            document.getString("belong")!!,
                            document.getString("image")!!
                        )
                    )
                    Log.d("test init","${document.getString("name")!!}")
                    count--
                    if (count == 0) break
                }
                setSlideViewPager()
            }
        return itemList
    }
    fun getViewByBranch(name : String): ArrayList<HomeDTO> {
        itemList.clear()
        var count = 10;
        db.collection("Reservation2")
            .document("store")
            .collection("restaurant")
            .get().addOnSuccessListener { documents ->
                for (document in documents.shuffled()) {
                    if(document.getString("belong") == name){
                        itemList.add(
                            HomeDTO(
                                document.get("name").toString(),
                                document.get("belong").toString(),
                                document.get("image").toString()
                            )
                        )
                        Log.d("test refresh","${document.getString("name")!!}")
                        count--
                    }
                    if (count == 0) break
                }
                setSlideViewPager()
            }
        return itemList
    }
    private fun setSlideViewPager(){
        adapter = HomeViewPagerAdapter(itemList)
        binding.sliderViewPager.adapter = adapter
        binding.sliderViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

}