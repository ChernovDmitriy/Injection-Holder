package com.github.chernovdmitriy.injectionholderappcompat

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.github.chernovdmitriy.injectionholdercore.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback

internal class FragmentAppCompatLifecycleCallback(
    private val componentCallback: ComponentCallback,
    private val fragmentStateStore: FragmentStateStore
) : FragmentManager.FragmentLifecycleCallbacks() {

    private fun addInjectionIfNeed(fragment: Fragment?) {
        if (fragment is ComponentOwner<*>) {
            if (!isInSaveState(fragment)) {
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
            if (!isInSaveState(fragment)) {
                componentCallback.removeInjection(fragment)
            }
            return
        }

        if (isInSaveState(fragment)) {
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
        setSaveState(f, false)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        setSaveState(f, false)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        setSaveState(f, true)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        removeInjectionIfNeed(f)
    }

    private fun isInSaveState(fragment: Fragment) = fragmentStateStore.getSaveState(fragment)

    private fun setSaveState(fragment: Fragment, isInSaveState: Boolean) =
        fragmentStateStore.setSaveState(fragment, isInSaveState)
}