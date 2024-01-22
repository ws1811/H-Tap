package kr.co.htap.navigation.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.co.htap.databinding.ActivityTimePickerBinding

class TimePickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimePickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "${intent.getIntExtra("year", 0)}년 ${intent.getIntExtra("month", 0)}월 ${intent.getIntExtra("day", 0)}일", Toast.LENGTH_SHORT).show()
    }
}