package kr.co.htap.navigation.location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.htap.databinding.ItemFragmentCheckLocationBinding
import kr.co.htap.navigation.reservation.BranchEntity

/**
 *
 * @author eunku
 */
class LocationRecyclerViewAdapter(private val locationProvider: LocationProvider,
                                  private val branchList : ArrayList<BranchEntity>)
    : RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemFragmentCheckLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LocationViewHolder(binding)
    }
    inner class LocationViewHolder(private val binding: ItemFragmentCheckLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(branchItem : BranchEntity){
            val branch_name = binding.itemTvBranch
            val branch_distance = binding.itemTvDistance
            branch_name.text =  branchItem.name
            locationProvider.getDistance(branchItem, branch_distance)
        }
    }
    override fun getItemCount(): Int = branchList.size
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(branchList[position])
    }
}