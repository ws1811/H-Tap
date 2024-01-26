package kr.co.htap.helper

/**
 *
 * @author 이호연
 */
fun interface OnFragmentReady<T : androidx.viewbinding.ViewBinding> {
    fun onFragmentReady(binding: T)
}