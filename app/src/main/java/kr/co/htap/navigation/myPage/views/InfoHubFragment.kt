package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import kr.co.htap.databinding.FragmentItemListBinding
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.navigation.myPage.adapter.RecyclerViewAdapter
import kr.co.htap.navigation.myPage.model.MenuItemContent
import kr.co.htap.navigation.myPage.model.MenuItem

/**
 * @author 호연
 */
class InfoHubFragment(val setLayout: (View) -> Unit = {}) :
    ViewBindingFragment<FragmentItemListBinding>() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentItemListBinding = FragmentItemListBinding.inflate(inflater, container, false)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        with(binding.list) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            overScrollMode = OVER_SCROLL_NEVER
            adapter = RecyclerViewAdapter(MenuItems)
            itemAnimator = null
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setLayout(view)
    }

    companion object {
        val MenuItems: MutableList<MenuItem> = ArrayList()

        init {
            val list = listOf(
                MenuItemContent("문의하기", { Log.d(toString(), "문의하기") }),
                MenuItemContent("FAQ", { Log.d(toString(), "FAQ") }),
                MenuItemContent("개인정보처리방침", { Log.d(toString(), "개인정보처리방침") })
            )
            for (i in 0..<list.size) {
                MenuItems.add(MenuItemContent.createMenuItem(i, list[i]))
            }
        }

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) = InfoHubFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }
}