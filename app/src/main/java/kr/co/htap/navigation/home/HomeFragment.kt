package kr.co.htap.navigation.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kr.co.htap.R
import kr.co.htap.databinding.FragmentHomeBinding
import kr.co.htap.navigation.NavigationActivity
import kr.co.htap.navigation.location.CheckLocationFragment
import kr.co.htap.navigation.location.LocationProvider
import kr.co.htap.navigation.myPage.model.ReservationHistory
import kr.co.htap.navigation.myPage.views.ModifyPersonalInfoFragment
import kr.co.htap.navigation.myPage.views.MyPageReservationHistoryFragment

/**
 *
 * @author eunku
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navigationActivity: NavigationActivity
    private lateinit var adapter: HomeViewPagerAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var branchName: String
    private lateinit var dbSetStart: Query
    private lateinit var dbSetRefresh: Query
    private lateinit var locationProvider: LocationProvider
    var itemList = ArrayList<HomeDTO>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationProvider = LocationProvider(context)
        navigationActivity = context as NavigationActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        dbSetStart = db.collection("Reservation")
            .document("store")
            .collection("restaurant")
            .limit(10)

        dbSetRefresh = db.collection("Reservation")
            .document("store")
            .collection("restaurant")

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("bundleKey")
            branchName = result!!
            getViewByBranch(branchName)
            navigationActivity.belong = branchName
            navigationActivity.isBelongData = false
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
//        var locationProvider = LocationProvider(Context.)
        if (locationProvider.checkPermission()) {
            locationProvider.getLocation()
        } else locationProvider.requestLocation()

        binding.btCheckReservation.setOnClickListener {
            with(requireActivity().supportFragmentManager.beginTransaction()) {
                setCustomAnimations(
                    R.anim.anim_slide_in_from_right_fade_in,
                    R.anim.anim_fade_out,
                    R.anim.anim_slide_in_from_left_fade_in,
                    R.anim.anim_fade_out
                )
                replace(R.id.mainFrameLayout, MyPageReservationHistoryFragment())
                addToBackStack(null)
                commit()
            }
        }
        binding.btFindBranch.setOnClickListener {
            val dialog = CheckLocationFragment(locationProvider)
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.show(requireActivity().supportFragmentManager, "CheckLocationFragment")
            }, 500)
        }
    }

    private fun getInitView(): ArrayList<HomeDTO> {

        dbSetStart
            .get().addOnSuccessListener { documents ->
                for (document in documents.shuffled()) {
                    itemList.add(
                        HomeDTO(
                            document.getString("name")!!,
                            document.getString("belong")!!,
                            document.getString("image")!!
                        )
                    )
                }
                setSlideViewPager()
            }
        return itemList
    }

    fun getViewByBranch(name: String): ArrayList<HomeDTO> {
        itemList.clear()
        db.collection("Reservation")
            .document("store")
            .collection("restaurant")
            .whereEqualTo("belong", "${name}")
            .limit(10)
            .get().addOnSuccessListener { documents ->
                for (document in documents.shuffled()) {
                    itemList.add(
                        HomeDTO(
                            document.get("name").toString(),
                            document.get("belong").toString(),
                            document.get("image").toString()
                        )
                    )
                }
                setSlideViewPager()
            }
        return itemList
    }

    private fun setSlideViewPager() {
        adapter = HomeViewPagerAdapter(itemList)
        binding.sliderViewPager.adapter = adapter
        binding.sliderViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}