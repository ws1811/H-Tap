package kr.co.htap.navigation.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.ItemFragmentCheckLocationBinding
import kr.co.htap.navigation.reservation.BranchEntity

/**
 *
 * @author eunku
 */
class LocationRecyclerViewAdapter(private val branchList : ArrayList<BranchEntity>)
    : RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder>() {
    private lateinit var locationProvider: LocationProvider
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemFragmentCheckLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        locationProvider = LocationProvider(parent.context)
        return LocationViewHolder(binding)
    }
    inner class LocationViewHolder(private val binding: ItemFragmentCheckLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val branch_name = binding.itemTvBranch
        val branch_distance = binding.itemTvDistance
        val root = binding.root

        fun bind(branchItem : BranchEntity){
            branch_name.text =  branchItem.name
            branch_distance.text = locationProvider.getDistances(branchItem)
            Log.d("bind_test", "${branchItem.name}, ${branch_distance.text}")
        }
    }
    override fun getItemCount(): Int = branchList.size
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(branchList[position])
    }
}