package kr.co.htap.navigation.home

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
class HomeViewPagerAdapter(private val itemList: ArrayList<HomeDTO>) :
    RecyclerView.Adapter<HomeViewPagerAdapter.PagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ItemFragmentMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    inner class PagerViewHolder(private val binding: ItemFragmentMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeDTO, num :Int) {
            Glide.with(itemView.context)
                .load(item.imgURL)
                .into(binding.slideImage)

            // 블러 효과 적용
            Glide.with(itemView.context)
                .load(item.imgURL)
                .transform(BlurTransformation(16, 4))
                .into(binding.blurImage)

            binding.textImageNumber.text = "${num+1}/${itemList.size}"
            binding.progressBar.progress = (num+1) * 100 / itemList.size
            binding.branchName.text = item.belong
            binding.storeName.text = item.name
            // TODO: 클릭 시 예약할 수 있는 페이지로 이동
            binding.slideImage.setOnClickListener{

            }
        }
    }
}