package kr.co.htap.navigation.reservation

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.play.core.integrity.p
import kr.co.htap.R
import kr.co.htap.databinding.ReservationListItemBinding
import kr.co.htap.navigation.reservation.DateSelect.DatePickerActivity

/**
 *
 * @author 김기훈
 *
 */

class ReservationListAdapter(private val storeList: ArrayList<StoreEntity>): RecyclerView.Adapter<ReservationListAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ReservationListItemBinding): RecyclerView.ViewHolder(binding.root) {
        val storeName = binding.storeName
        val storeDescription = binding.storeDescription
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
        holder.storeDescription.text = storeData.description
        Glide.with(holder.root.context).load(storeData.image).into(holder.storeImage)

        holder.root.setOnClickListener {
            val intent = Intent(holder.root.context, DatePickerActivity::class.java)
            intent.putExtra("name", storeData.name)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            holder.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return storeList.size
    }
}