package kr.co.htap.navigation.reservation

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.htap.MainActivity
import kr.co.htap.R
import kr.co.htap.databinding.ActivityMainBinding
import kr.co.htap.databinding.FragmentReservationBinding
import kr.co.htap.navigation.NavigationActivity

/**
 *
 * @author 김기훈
 *
 */
class ReservationFragment : Fragment() {

    private lateinit var binding: FragmentReservationBinding
    private lateinit var navigationActivity: NavigationActivity
    private lateinit var adapter: ReservationListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationActivity = context as NavigationActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReservationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSelectButton()
    }

    private fun setRecyclerView(storeData: ArrayList<StoreEntity>) {
        navigationActivity.runOnUiThread {
            adapter = ReservationListAdapter(storeData)
            binding.reservationRecyclerView.adapter = adapter
            binding.reservationRecyclerView.layoutManager = LinearLayoutManager(navigationActivity)
        }
    }

    private fun setSelectButton() {
        binding.restaurentButton.setOnClickListener {
            setRecyclerView(configureFoodInitData())
        }

        binding.laundryButton.setOnClickListener {
            setRecyclerView(configureLaundryInitData())
        }

        binding.restaurentButton.performClick()
    }

    private fun configureFoodInitData(): ArrayList<StoreEntity> {
        var data: ArrayList<StoreEntity> = ArrayList()

        data.add(
            StoreEntity(
                "카페",
                "스타벅스",
                "영업중",
                "https://i.namu.wiki/i/9p8OVxJTce_f2HnuZF1QOU6qMSHqXBHdkcx3q_hlGxvhcyaOXKxBVyoDkeg-Cb4Nx2p60W0AUh6RzjAH59vHwQ.svg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "카페",
                "스타벅스000",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "카페",
                "스타벅스111",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "카페",
                "스타벅스222",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "카페",
                "스타벅스333",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "카페",
                "스타벅스444",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "카페",
                "스타벅스555",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )

        data.add(
            StoreEntity(
                "카페",
                "스타벅스666",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )

        return data
    }

    private fun configureLaundryInitData(): ArrayList<StoreEntity> {
        var data: ArrayList<StoreEntity> = ArrayList()

        data.add(
            StoreEntity(
                "세탁",
                "콩순이 세탁소",
                "영업중",
                "https://i.namu.wiki/i/9p8OVxJTce_f2HnuZF1QOU6qMSHqXBHdkcx3q_hlGxvhcyaOXKxBVyoDkeg-Cb4Nx2p60W0AUh6RzjAH59vHwQ.svg",
                12312412
            )
        )
        data.add(
            StoreEntity(
                "수선",
                "콩순이 수선소",
                "영업중",
                "https://cdn.pixabay.com/photo/2017/08/06/12/06/people-2591874_960_720.jpg",
                12312412
            )
        )

        return data
    }
}