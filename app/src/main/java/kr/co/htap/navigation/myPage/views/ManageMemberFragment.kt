package kr.co.htap.navigation.myPage.views

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import android.view.ViewGroup
import kr.co.htap.R
import kr.co.htap.databinding.FragmentItemListBinding
import kr.co.htap.helper.ViewBindingFragment
import kr.co.htap.navigation.myPage.adapter.RecyclerViewAdapter
import kr.co.htap.navigation.myPage.model.MenuItemContent
import kr.co.htap.navigation.myPage.model.MenuItem

/**
 * @author 호연
 */
class ManageMemberFragment(val setLayout: (View) -> Unit = {}) :
    ViewBindingFragment<FragmentItemListBinding>() {

    private var columnCount = 1

    private val MenuItems: MutableList<MenuItem> = ArrayList()
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

        val list = listOf(
            MenuItemContent("개인정보 수정") {
                Log.d(Companion.toString(), "개인정보 수정")
                with(requireActivity().supportFragmentManager.beginTransaction()) {
                    setCustomAnimations(
                        R.anim.anim_slide_in_from_right_fade_in,
                        R.anim.anim_fade_out,
                        R.anim.anim_slide_in_from_left_fade_in,
                        R.anim.anim_fade_out
                    )
                    replace(R.id.mainFrameLayout, ModifyPersonalInfoFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        )
        for (i in list.indices) {
            MenuItems.add(MenuItemContent.createMenuItem(i, list[i]))
        }

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


        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) = ManageMemberFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }
}