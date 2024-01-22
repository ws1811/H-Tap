package kr.co.htap.helper

import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author  이호연
 */
class SimpleCallback<T>(
    private val _onResponse: OnResponse<T>,
    private val _onFailure: OnFailure<T> = OnFailure.printStackTrace()
) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        _onResponse.onResponse(call, response)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        _onFailure.onFailure(call, t)
    }

}

/**
 * @author  이호연
 */
fun interface OnResponse<T> {
    fun onResponse(call: Call<T>, response: Response<T>)
}

/**
 * @author 이호연
 */
fun interface OnFailure<T> {
    fun onFailure(call: Call<T>?, t: Throwable?)


    companion object {
        //실패한 경우 printStacktrace만 하는 익명 SAM 리턴
        fun <T> printStackTrace(): OnFailure<T> {
            return OnFailure { _, t -> t?.printStackTrace() }
        }

        //실패한 경우 로그를 출력하는 익명 SAM 리턴
        fun <T> printLog(tag: String, msg: String): OnFailure<T> {
            return OnFailure { _, _ -> Log.e(tag, msg) }
        }

        //실패한 경우 토스트를 출력하는 익명 SAM 리턴
        fun <T> showToast(toast: Toast): OnFailure<T> {
            return OnFailure { _, _ -> toast.show() }
        }
    }
}

