package kr.co.htap.helper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 *
 * @author 이호연
 */
abstract class ViewBindingActivity<R : ViewBinding>
    : AppCompatActivity() {
    private lateinit var _binding: R
    protected val binding: R get() = _binding
    abstract fun initViewBinding(): R
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initViewBinding()
        setContentView(binding.root)

    }
}