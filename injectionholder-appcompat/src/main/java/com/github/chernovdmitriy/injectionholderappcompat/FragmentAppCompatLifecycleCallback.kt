package com.github.chernovdmitriy.injectionholderappcompat

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import com.github.chernovdmitriy.injectionholdercore.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback
import java.util.concurrent.atomic.AtomicBoolean

internal class FragmentAppCompatLifecycleCallback(
    private val componentCallback: ComponentCallback
) : FragmentManager.FragmentLifecycleCallbacks() {

    private val isInSaveState = AtomicBoolean(false)

    private fun addInjectionIfNeed(fragment: Fragment?) {
        if (fragment is ComponentOwner<*>) {
            if (!isInSaveState.get()) {
                componentCallback.removeInjection(fragment)
            }
            componentCallback.addInjection(fragment)
        }
    }

    private fun removeInjectionIfNeed(fragment: Fragment?) {
        if (fragment !is ComponentOwner<*>) {
            return
        }

        if (fragment.activity?.isFinishing == true) {
            if (!isInSaveState.get()) {
                componentCallback.removeInjection(fragment)
            }
            return
        }

        if (isInSaveState.get()) {
            isInSaveState.set(false)
            return
        }

        var anyParentIsRemoving = false
        var parent = fragment.parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }
        if (fragment.isRemoving || anyParentIsRemoving) {
            componentCallback.removeInjection(fragment)
        }
    }


    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        addInjectionIfNeed(f)
        super.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        isInSaveState.set(false)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        isInSaveState.set(false)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        isInSaveState.set(true)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        removeInjectionIfNeed(f)
    }

}