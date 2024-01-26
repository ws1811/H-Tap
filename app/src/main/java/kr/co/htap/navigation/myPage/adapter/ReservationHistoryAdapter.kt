package kr.co.htap.navigation.myPage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.htap.databinding.ItemReservationHistoryBinding
import kr.co.htap.navigation.myPage.model.ReservationHistory

/**
 *
 * @author 이호연
 */
class ReservationHistoryAdapter(var history: List<ReservationHistory>) :
    RecyclerView.Adapter<ReservationHistoryAdapter.ReservationHistoryViewHolder>() {
    inner class ReservationHistoryViewHolder(val binding: ItemReservationHistoryBinding) :
        ViewHolder(binding.root) {
        fun bind(history: ReservationHistory) {
            with(binding) {
                if (history.imageUrl.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(history.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.imageViewStore)
                }
                labelBranch.text = history.branchName
                labelStore.text = history.storeName
                labelTime.text = history.time
                labelDate.text = history.date
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationHistoryViewHolder {
        return ReservationHistoryViewHolder(
            ItemReservationHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = history.size

    override fun onBindViewHolder(holder: ReservationHistoryViewHolder, position: Int) {
        holder.bind(history[position])
    }
}