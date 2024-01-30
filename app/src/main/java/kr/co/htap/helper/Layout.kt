package kr.co.htap.helper

import android.view.View
import android.widget.LinearLayout

/**
 *
 * @author 이호연
 */
class Layout {
    companion object {
        fun setMarginTop(marginInDP: Int): (View) -> Unit {
            return { view ->
                val layoutParams = view.layoutParams as LinearLayout.LayoutParams
                layoutParams.setMargins(0, marginInDP, 0, 0) // 상단 마진을 설정합니다.
                view.layoutParams = layoutParams
            }
        }
    }
}