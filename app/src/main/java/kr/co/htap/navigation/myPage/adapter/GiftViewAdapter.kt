package kr.co.htap.navigation.myPage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.FragmentGiftItemBinding
import kr.co.htap.navigation.myPage.model.Gift

/**
 *
 * @author 호연
 */
class GiftViewAdapter(private val gifts: List<Gift>) :
    RecyclerView.Adapter<GiftViewAdapter.GiftViewHolder>() {
    inner class GiftViewHolder(val binding: FragmentGiftItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setExpireDate(expires: String) {
            binding.tvExpires.text = expires
        }

        fun setProductName(productName: String) {
            binding.tvProductName.text = productName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        return GiftViewHolder(
            FragmentGiftItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = gifts.size


    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        with(gifts[position]) {
            holder.setExpireDate(expires)
            holder.setProductName(productName)
        }
    }
}

