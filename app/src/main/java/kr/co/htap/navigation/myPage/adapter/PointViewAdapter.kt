package kr.co.htap.navigation.myPage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.FragmentPointHistoryBinding
import kr.co.htap.navigation.myPage.model.PointHistory

/**
 *
 * @author 호연
 */
class PointViewAdapter(private val history: List<PointHistory>) :
    RecyclerView.Adapter<PointViewAdapter.PointViewHolder>() {
    inner class PointViewHolder(val binding: FragmentPointHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setPoint(point: Int) {
            binding.tvPointPoint.text = String.format("%d p", point)
        }

        fun setCause(cause: String) {
            binding.tvPointCause.text = cause
        }

        fun setType(type: String) {
            binding.tvPointType.text = type
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        return PointViewHolder(
            FragmentPointHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = history.size


    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        with(history[position]) {
            holder.setPoint(point)
            holder.setCause(cause)
            holder.setType(type)
        }
//        notifyItemChanged(position)
    }
}

