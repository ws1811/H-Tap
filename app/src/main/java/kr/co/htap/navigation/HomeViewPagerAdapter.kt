package kr.co.htap.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.BlurTransformation
import kr.co.htap.databinding.ItemFragmentMainBinding

/**
*
* @author eunku
*/
class HomeViewPagerAdapter(private val foodList: ArrayList<Int>) :
    RecyclerView.Adapter<HomeViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ItemFragmentMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = foodList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    inner class PagerViewHolder(private val binding: ItemFragmentMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: Int) {
            // Load the image into the slideImage
            Glide.with(itemView.context)
                .load(foodItem)
                .into(binding.slideImage)

            // Apply blur effect to the blurImage using Glide Transformations
            Glide.with(itemView.context)
                .load(foodItem)
                .transform(BlurTransformation(16, 4))
                .into(binding.blurImage)
        }
    }


}