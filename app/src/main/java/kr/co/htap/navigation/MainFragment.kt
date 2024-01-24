package kr.co.htap.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kr.co.htap.R
import kr.co.htap.databinding.FragmentMainBinding
import kr.co.htap.navigation.location.CheckLocationFragment
import kr.co.htap.navigation.location.LocationProvider

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navigationActivity: NavigationActivity
    private lateinit var adapter: HomeViewPagerAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationActivity = context as NavigationActivity
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

        // Sample data for the food items
        val foodList = arrayListOf(
            R.drawable.food1,
            R.drawable.food2,
            R.drawable.food3,
            // Add more food items as needed
        )

        // Initialize the ViewPagerAdapter with the foodList
        adapter = HomeViewPagerAdapter(foodList)
        binding.sliderViewPager.adapter = adapter
        binding.sliderViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        var locationProvider = LocationProvider(navigationActivity)
        locationProvider.getLocation()

        binding.btFindBranch.setOnClickListener {
            val dialog = CheckLocationFragment(locationProvider)
            dialog.show(requireActivity().supportFragmentManager, "CheckLocationFragment")
        }

    }

}