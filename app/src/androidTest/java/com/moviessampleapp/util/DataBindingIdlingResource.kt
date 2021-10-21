package com.moviessampleapp.util

/**
 * Created by Nadeem on 20/10/2021.
 */
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.test.espresso.IdlingResource
import java.util.*

/**
 * An espresso idling resource implementation that reports idle status for all data binding
 * layouts.
 * <b/>
 */
class DataBindingIdlingResource(
    private val fragment: Fragment
) : IdlingResource {
    // list of registered callbacks
    private val idlingCallbacks = mutableListOf<IdlingResource.ResourceCallback>()

    // give it a unique id to workaround an espresso bug where you cannot register/unregister
    // an idling resource w/ the same name.
    private val id = UUID.randomUUID().toString()

    // holds whether isIdle is called and the result was false. We track this to avoid calling
    // onTransitionToIdle callbacks if Espresso never thought we were idle in the first place.
    private var wasNotIdle = false

    override fun getName() = "DataBinding $id"

    override fun isIdleNow(): Boolean {
        val idle = !getBindings().any { it.hasPendingBindings() }
        @Suppress("LiftReturnOrAssignment")
        if (idle) {
            if (wasNotIdle) {
                // notify observers to avoid espresso race detector
                idlingCallbacks.forEach { it.onTransitionToIdle() }
            }
            wasNotIdle = false
        } else {
            wasNotIdle = true
            // check next frame
            fragment.view?.findViewById<View>(android.R.id.content)?.postDelayed(
                {
                    isIdleNow
                }, 16
            )
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        idlingCallbacks.add(callback)
    }

    private fun getBindings(): List<ViewDataBinding> {
        return fragment.view?.let {
            DataBindingUtil.getBinding<ViewDataBinding>(it)?.let { binding ->
                listOf(binding)
            } ?: emptyList()
        } ?: emptyList()
    }
}