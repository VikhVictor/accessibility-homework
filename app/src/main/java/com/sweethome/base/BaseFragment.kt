package com.sweethome.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.sweethome.MainActivity
import com.sweethome.R
import com.sweethome.RootRouter
import com.sweethome.SweetHomeApplication

abstract class BaseFragment : Fragment() {

    protected val application: SweetHomeApplication
        get() = requireActivity().application as SweetHomeApplication

    protected val rootRouter: RootRouter
        get() = (requireActivity() as MainActivity).rootRouter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId(), container, false);
        onViewInflated(view)
        return view
    }

    @CallSuper
    open fun onViewInflated(view: View) {
        val title = view.findViewById<TextView>(R.id.screen_title)
        title?.text = title()
        val back = view.findViewById<View>(R.id.back_button)
        back?.setOnClickListener {
            activity?.onBackPressed()
        }
        val shoppingCart = view.findViewById<View>(R.id.cart_button)
        if (showCart()) {
            shoppingCart.visibility = View.VISIBLE
        }
    }

    abstract fun layoutId(): Int
    abstract fun title(): String
    open fun showCart(): Boolean {
        return false
    }
}
