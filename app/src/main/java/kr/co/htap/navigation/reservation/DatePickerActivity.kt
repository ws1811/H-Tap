package kr.co.htap.navigation.reservation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import kr.co.htap.R
import kr.co.htap.databinding.ActivityDatePickerBinding
import java.util.Calendar

/**
 *
 * @author 김기훈
 *
 */

class DatePickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatePickerBinding

    private lateinit var calendar: Calendar
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra("id", 0)

        configureDate()
        setUI()
    }

    private fun configureDate() {
        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun setUI() {
        binding.title.text = intent.getStringExtra("title")

        binding.calendarView.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 10)
        binding.calendarView.maxDate = calendar.timeInMillis

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            this.year = year
            this.month = month + 1
            this.day = dayOfMonth
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.confirmButton.setOnClickListener {
            var intent = Intent(this, TimePickerActivity::class.java)

            intent.putExtra("storeName", binding.title.text)
            intent.putExtra("id", id)
            intent.putExtra("year", year)
            intent.putExtra("month", month)
            intent.putExtra("day", day)

            startActivity(intent)
        }
    }
}