package kr.co.htap.navigation.home

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
import kr.co.htap.databinding.FragmentHomeBinding
import kr.co.htap.navigation.NavigationActivity
import kr.co.htap.navigation.location.CheckLocationFragment
import kr.co.htap.navigation.location.LocationProvider

/**
 *
 * @author eunku
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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

        setFragmentResultListener("requestKey"){ requestKey, bundle ->
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
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInitView()
        var locationProvider = LocationProvider(navigationActivity)
        locationProvider.getLocation()
        binding.btFindBranch.setOnClickListener {
            locationProvider.requestLocation() // 플로우 체크
            val dialog = CheckLocationFragment(locationProvider)
            dialog.show(requireActivity().supportFragmentManager, "CheckLocationFragment")
        }
    }

    private fun getInitView(): ArrayList<HomeDTO> {
        itemList.clear()
        var count = 10;
        db.collection("Reservation")
            .document("store")
            .collection("restaurant")
            .get().addOnSuccessListener { documents ->
                Log.d("test init","1234")
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
        db.collection("Reservation")
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