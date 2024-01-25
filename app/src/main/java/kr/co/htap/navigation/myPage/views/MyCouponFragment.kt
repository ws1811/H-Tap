package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kr.co.htap.databinding.FragmentMyPageCouponBinding
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.navigation.myPage.adapter.CouponViewAdapter
import kr.co.htap.navigation.myPage.model.Coupon

/**
 * @author 호연
 */
class MyCouponFragment : ViewBindingFragment<FragmentMyPageCouponBinding>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var gson: Gson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
        gson = Gson()
    }

    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentMyPageCouponBinding = FragmentMyPageCouponBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val coupons: MutableList<Coupon> = mutableListOf()
        firestore.document("coupons/${auth.currentUser?.uid}").get().addOnCompleteListener {
            with(it.result) {
                if (exists()) {
                    val stringArray: List<String>? = get("coupons") as? List<String>
                    stringArray?.forEach {
                        try {
                            coupons.add(gson.fromJson(it, Coupon::class.java))
                        } catch (_: Exception) {

                        }
                    }

                }
                requireActivity().runOnUiThread {
                    with(binding.recyclerView) {
                        adapter = CouponViewAdapter(coupons)
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }


    }

    companion object {

    }
}