package kr.co.htap.navigation.reservation

import android.app.DatePickerDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.htap.R
import kr.co.htap.databinding.ReservationListItemBinding
import kr.co.htap.navigation.reservation.TimeSelect.TimePickerActivity
import java.util.Calendar

/**
 *
 * @author 김기훈
 *
 */

class ReservationListAdapter(private val storeList: ArrayList<StoreEntity>): RecyclerView.Adapter<ReservationListAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ReservationListItemBinding): RecyclerView.ViewHolder(binding.root) {
        val storeName = binding.storeName
        val storeBelong = binding.storeBelong
        val storeImage = binding.storeImage

        var root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ReservationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storeData = storeList[position]

        holder.storeName.text = storeData.name
        holder.storeBelong.text = storeData.belong
        Glide
            .with(holder.root.context)
            .load(storeData.image)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(holder.storeImage)

        holder.itemView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                holder.itemView.context,
                R.style.CustomDatePickerDialog,
                { _, year, month, dayOfMonth ->
                    val selectedDate = DateDTO(year, month + 1, dayOfMonth)
                    val intent = Intent(holder.itemView.context, TimePickerActivity::class.java)
                    intent.putExtra("name", storeData.name)
                    intent.putExtra("belong", storeData.belong)
                    intent.putExtra("date", selectedDate)
                    intent.putExtra("documentId", storeData.documentId)
                    startActivity(holder.itemView.context, intent, null) },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_MONTH, 10)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.window?.setBackgroundDrawableResource(R.drawable.shape_round_dialog)
            datePickerDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return storeList.size
    }
}