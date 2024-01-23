package kr.co.htap.navigation.reservation.TimeSelect

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.ReservationTimeButtonBinding


/**
 *
 * @author 김기훈
 *
 */
class TimeListAdapter(private val timeList: ArrayList<TimeDTO>): RecyclerView.Adapter<TimeListAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ReservationTimeButtonBinding): RecyclerView.ViewHolder(binding.root) {
        val button = binding.button

        var root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ReservationTimeButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val minute: String = if (timeList[position].minute < 10) "0" + timeList[position].minute else timeList[position].minute.toString()
        holder.button.text = timeList[position].hour.toString() + ":" + minute
        holder.button.paintFlags = if (timeList[position].isAvailable) 0 else Paint.STRIKE_THRU_TEXT_FLAG
        holder.button.isEnabled = timeList[position].isAvailable

        holder.button.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}