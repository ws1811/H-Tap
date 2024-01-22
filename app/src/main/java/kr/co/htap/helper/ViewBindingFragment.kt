package kr.co.htap.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 *
 * @author 이호연
 */
abstract class ViewBindingFragment<R : ViewBinding> : Fragment() {
    private var _binding: R? = null
    protected val binding: R get() = _binding!!

    abstract fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): R

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}