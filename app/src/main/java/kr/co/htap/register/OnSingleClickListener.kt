package kr.co.htap.register

import android.os.SystemClock
import android.view.View
import android.widget.Toast

/**
 *
 * @author 송원선
 * 중복 클릭 방지 추상 클래스
 */
abstract class OnSingleClickListener : View.OnClickListener {
    private var mLastClickTime: Long = 0
    abstract fun onSingleClick(v: View?)
    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        // 중복클릭 아닌 경우
        if (elapsedTime > MIN_CLICK_INTERVAL) {
            onSingleClick(v)
        } else {
            Toast.makeText(v?.context, "요청을 처리중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT)
        }
    }

    companion object {
        // 중복 클릭 방지 시간 설정 ( 해당 시간 이후에 다시 클릭 가능 )
        private const val MIN_CLICK_INTERVAL: Long = 2000 // 2초
    }
}