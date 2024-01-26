package kr.co.htap.navigation.location

import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import kr.co.htap.databinding.FragmentCheckLocationBinding

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
        binding = FragmentCheckLocationBinding.inflate(inflater, container, false)
        if(branchList.isEmpty()){
            configureInitData()
        } else setBranchList()

        binding.btnRefreshLocation.setOnClickListener {
            setBranchList()
        }
        binding.btnBackMain.setOnClickListener{
            parentFragmentManager.beginTransaction().remove(this@CheckLocationFragment).commit()//현재 프레그먼트 닫기
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetricsPointCompat()
//        var deviceWidth = size.x
        var deviceHeight = size.y

//        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceHeight * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams

    }
    fun WindowManager.currentWindowMetricsPointCompat() : Point {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            val windowInsets = currentWindowMetrics.windowInsets
            var insets : Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
            windowInsets.displayCutout?.run{
                insets = Insets.max(
                    insets,
                    Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
                )
            }
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom
            Point(
                currentWindowMetrics.bounds.width() - insetsWidth,
                currentWindowMetrics.bounds.height() - insetsHeight
            )
        } else {
            Point().apply {
                defaultDisplay.getSize(this)
            }
        }
    }


    private fun setBranchList() {
        locationProvider.updateLocation()
        adapter = LocationRecyclerViewAdapter(locationProvider, branchList)

        adapter.itemClickListener = object : LocationRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(name : String) {
                val result = name
                setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                parentFragmentManager.beginTransaction().remove(this@CheckLocationFragment).commit()//현재 프레그먼트 닫기
            }
        }
        binding.locationRecycler.adapter = adapter
        binding.locationRecycler.layoutManager = LinearLayoutManager(context)
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
            setBranchList()
        }.addOnFailureListener { exception ->
            Log.d("test", "불러오기 실패")
        }
        return branchList
    }


}