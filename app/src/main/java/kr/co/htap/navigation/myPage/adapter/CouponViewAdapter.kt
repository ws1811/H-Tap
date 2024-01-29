package kr.co.htap.navigation.myPage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.htap.databinding.FragmentCouponItemBinding
import kr.co.htap.navigation.myPage.model.Coupon

/**
 *
 * @author 호연
 */
class CouponViewAdapter(private val coupons: List<Coupon>) :
    RecyclerView.Adapter<CouponViewAdapter.CouponViewHolder>() {
    inner class CouponViewHolder(val binding: FragmentCouponItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setImageWithUrl(imageUrl: String) {
            if (imageUrl.isNotEmpty())
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.imgGift)
        }

        fun setExpireDate(expires: String) {
            binding.tvExpires.text = expires
        }

        fun setProductName(productName: String) {
            binding.tvProductName.text = productName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        return CouponViewHolder(
            FragmentCouponItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = coupons.size


    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        with(coupons[position]) {
            holder.setExpireDate(expires)
            holder.setImageWithUrl(productImgUrl)
            holder.setProductName(productName)
        }
    }
}

