package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.util.Log
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

    override fun onResume() {
        super.onResume()
        val recyclerItems: MutableList<Coupon> = mutableListOf()
        val userDocPath = "/users/${auth.currentUser?.email}"
        val couponPath = "/coupons"
        firestore.runTransaction { transaction ->
            val userDocRef = firestore.document(userDocPath)
            val user = transaction.get(userDocRef)
            val coupons = user.get("coupons") as? List<String> ?: arrayListOf()
            if (coupons.isEmpty()) return@runTransaction
            coupons.forEach {
                Log.d(this@MyCouponFragment.toString(), it)
                val splitted = it.split(":")
                if (splitted.size < 2) return@forEach
                val couponId = splitted[0]
                val expires = splitted[1]
                val couponRef = firestore.document("${couponPath}/${couponId}")
                val coupon = transaction.get(couponRef)
                if (coupon.exists()) {
                    val imageUrl = coupon.getString("image") ?: ""
                    val name = coupon.getString("name") ?: ""
                    recyclerItems.add(Coupon(imageUrl, name, "${expires}까지"))
                }
            }
        }.addOnSuccessListener {
            requireActivity().runOnUiThread {
                binding.tvAvailableCouponCount.text = String.format("보유쿠폰 %d장", recyclerItems.size)
                with(binding.recyclerView) {
                    adapter = CouponViewAdapter(recyclerItems)
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    companion object {

    }
}