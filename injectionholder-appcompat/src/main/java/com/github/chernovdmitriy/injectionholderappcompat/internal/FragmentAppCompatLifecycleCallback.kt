package com.github.chernovdmitriy.injectionholderappcompat.internal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner

internal class FragmentAppCompatLifecycleCallback(
    private val componentManager: ComponentManager,
    private val fragmentStateStore: FragmentStateStore
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentPreCreated(fm, f, savedInstanceState)
        addInjectionIfNeed(f, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        println("onFragmentStarted, fm: $fm, f: $f")
        super.onFragmentStarted(fm, f)
        clearSaveState(f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        println("onFragmentResumed, fm: $fm, f: $f")
        super.onFragmentResumed(fm, f)
        clearSaveState(f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)
        saveFragmentState(f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        removeInjectionIfNeed(f)
    }

    private fun addInjectionIfNeed(fragment: Fragment, savedInstanceState: Bundle?) {
        if (fragment is ComponentOwner<*>) {
            if (!isInSaveState(fragment)) {
                componentManager.removeInjection(fragment)
            }
            componentManager.addInjection(fragment, savedInstanceState ?: fragment.arguments)
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
        (fragment as? ComponentOwner<*>)?.apply { componentManager.removeInjection(this) }
        clearSaveState(fragment)
    }

    private fun isInSaveState(fragment: Fragment) = fragmentStateStore.getSaveState(fragment)

    private fun saveFragmentState(fragment: Fragment) = fragmentStateStore.setSaveState(fragment, true)

    private fun clearSaveState(fragment: Fragment) = fragmentStateStore.setSaveState(fragment, false)
}