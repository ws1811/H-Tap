package kr.co.htap.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import kr.co.htap.databinding.FragmentMainBinding
import kr.co.htap.navigation.location.CheckLocationFragment
import kr.co.htap.navigation.location.HomeDTO
import kr.co.htap.navigation.location.LocationProvider

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navigationActivity: NavigationActivity
    private lateinit var adapter: HomeViewPagerAdapter
    private lateinit var db: FirebaseFirestore
    var itemList = ArrayList<HomeDTO>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationActivity = context as NavigationActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
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


        getInitData()

        var locationProvider = LocationProvider(navigationActivity)
        locationProvider.getLocation()

        binding.btFindBranch.setOnClickListener {
            val dialog = CheckLocationFragment(locationProvider)
            dialog.show(requireActivity().supportFragmentManager, "CheckLocationFragment")
        }
    }

    private fun getInitData(): ArrayList<HomeDTO> {
        var count = 10;
        db.collection("Reservation2")
            .document("store")
            .collection("restaurant")
            .get().addOnSuccessListener { documents ->

                for (document in documents.shuffled()) {
                    itemList.add(
                        HomeDTO(
                            document.get("name").toString(),
                            document.get("belong").toString(),
                            document.get("image").toString()
                        )
                    )
                    count--
                    if (count == 0) break
                }
                adapter = HomeViewPagerAdapter(itemList)
                binding.sliderViewPager.adapter = adapter
                binding.sliderViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//                binding.sliderViewPager.setPageTransformer(ZoomOutPageTransformer())
            }
        return itemList
    }


}