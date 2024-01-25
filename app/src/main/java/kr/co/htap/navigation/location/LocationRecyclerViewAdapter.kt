package kr.co.htap.navigation.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.ItemFragmentCheckLocationBinding
import javax.security.auth.callback.Callback

/**
 *
 * @author eunku
 */
class LocationRecyclerViewAdapter(
    private val locationProvider: LocationProvider,
    private val branchList: ArrayList<BranchEntity>

) : RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder>() {
    init {
        sortBranchByDistance()
    }

    interface OnItemClickListener {
        fun onItemClick(name: String) {}
    }

    var itemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemFragmentCheckLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LocationViewHolder(binding)
    }
    fun setCallback(callback: OnItemClickListener){
        this.itemClickListener= callback
    }

    inner class LocationViewHolder(private val binding: ItemFragmentCheckLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(branchItem: BranchEntity) {
            val branch_name = binding.itemTvBranch
            val branch_distance = binding.itemTvDistance
            branch_name.text = branchItem.name
            branch_distance.text = String.format("%.2fkm", branchItem.distance / 1000)

            branch_name.setOnClickListener {
                Log.d("test", "클릭 이벤트")
                itemClickListener?.onItemClick(branch_name.text.toString())
            }
        }
    }

    override fun getItemCount(): Int = branchList.size
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {

        holder.bind(branchList[position])


    }

    fun sortBranchByDistance() {
        branchList.forEach { item ->
            item.distance = locationProvider.getDistance(item)
        }
        branchList.sortBy { it.distance }
    }

}
