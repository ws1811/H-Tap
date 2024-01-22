package kr.co.htap.navigation.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.htap.databinding.ActivityTimePickerBinding

/**
 *
 * @author 김기훈
 *
 */
class TimePickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimePickerBinding

    private lateinit var adapter: TimeListAdapter
    private var timeData: ArrayList<TimeEntity> = arrayListOf(TimeEntity(11, 30), TimeEntity(12, 0), TimeEntity(12, 30), TimeEntity(13, 0),
        TimeEntity(13, 30), TimeEntity(14, 0, false), TimeEntity(14, 30), TimeEntity(15, 0),
        TimeEntity(15, 30), TimeEntity(16, 0), TimeEntity(16, 30), TimeEntity(17, 0),
        TimeEntity(17, 30), TimeEntity(18, 0), TimeEntity(18, 30, false), TimeEntity(19, 0),
        TimeEntity(19, 30), TimeEntity(20, 0))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
    }

    private fun setUI() {
        binding.title.text = intent.getStringExtra("storeName")

        setRecyclerView()
    }

    private fun setRecyclerView() {
        adapter = TimeListAdapter(timeData)
        binding.reservationTimeRecyclerView.adapter = adapter
        binding.reservationTimeRecyclerView.layoutManager = GridLayoutManager(this, 3)
    }
}