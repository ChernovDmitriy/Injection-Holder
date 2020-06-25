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

    private var bundle: Bundle? = null

    private fun addInjectionIfNeed(fragment: Fragment?) {
        if (fragment is ComponentOwner<*>) {
            if (!isInSaveState(fragment)) {
                componentCallback.removeInjection(fragment)
            }
            componentCallback.addInjection(fragment, bundle ?: fragment.arguments)
        }
    }

    private fun removeInjectionIfNeed(fragment: Fragment?) {
        if (fragment !is ComponentOwner<*>) {
            return
        }

        if (fragment.activity?.isFinishing == true) {
            if (!isInSaveState(fragment)) {
                clearInjection(fragment)
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
            clearInjection(fragment)
        }
    }

    private fun clearInjection(fragment: Fragment) {
        (fragment as? ComponentOwner<*>)?.apply { componentCallback.removeInjection(this) }
        clearSaveState(fragment)
        bundle = null
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        bundle = savedInstanceState
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        addInjectionIfNeed(f)
        super.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        clearSaveState(f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        clearSaveState(f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        saveFragmentState(f)
        bundle = outState
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        removeInjectionIfNeed(f)
    }

    private fun isInSaveState(fragment: Fragment) = fragmentStateStore.getSaveState(fragment)

    private fun saveFragmentState(fragment: Fragment) = fragmentStateStore.setSaveState(fragment, true)

    private fun clearSaveState(fragment: Fragment) = fragmentStateStore.setSaveState(fragment, false)
}