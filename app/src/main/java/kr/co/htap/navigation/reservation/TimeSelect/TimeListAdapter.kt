package kr.co.htap.navigation.reservation.TimeSelect

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.ReservationTimeButtonBinding
import kr.co.htap.navigation.reservation.DateSelect.DateDTO
import kr.co.htap.navigation.reservation.DateSelect.DatePickerActivity
import kr.co.htap.navigation.reservation.Loading.ReservationLoadingActivity


/**
 *
 * @author 김기훈
 *
 */
class TimeListAdapter(private val timeList: ArrayList<TimeDTO>,
                      private val storeName: String,
                      private val date: DateDTO): RecyclerView.Adapter<TimeListAdapter.MyViewHolder>() {
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
            val intent = Intent(holder.root.context, ReservationLoadingActivity::class.java)
            val month = if (date.month < 10) "0" + date.month else date.month.toString()

            intent.putExtra("name", storeName)
            intent.putExtra("date", date.year.toString() + "-" + month + "-" + date.day.toString())
            intent.putExtra("time", timeList[position].hour.toString() + ":" + minute)
            holder.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}