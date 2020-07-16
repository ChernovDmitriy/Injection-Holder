package com.github.chernovdmitriy.injectionholderx.internal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner

internal class FragmentXLifecycleCallback(
    private val componentManager: ComponentManager,
    private val fragmentStateStore: FragmentStateStore
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        log("onFragmentPreAttached, fm: $fm, f: $f, context: $context")
        super.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        log("onFragmentAttached, fm: $fm, f: $f, context: $context")
        super.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        log("onFragmentPreCreated, fm: $fm, f: $f, savedInstanceState: $savedInstanceState")
        super.onFragmentPreCreated(fm, f, savedInstanceState)
        addInjectionIfNeed(f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        log("onFragmentCreated, fm: $fm, f: $f, savedInstanceState: $savedInstanceState")
        super.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        log("onFragmentViewCreated, fm: $fm, f: $f, v: $v, savedInstanceState: $savedInstanceState")
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        log("onFragmentActivityCreated, fm: $fm, f: $f, savedInstanceState: $savedInstanceState")
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        log("onFragmentStarted, fm: $fm, f: $f")
        super.onFragmentStarted(fm, f)
        clearSaveState(f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        log("onFragmentResumed, fm: $fm, f: $f")
        super.onFragmentResumed(fm, f)
        clearSaveState(f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        log("onFragmentPaused, fm: $fm, f: $f")
        super.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        log("onFragmentStopped, fm: $fm, f: $f")
        super.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        log("onFragmentSaveInstanceState, fm: $fm, f: $f, outState: $outState")
        super.onFragmentSaveInstanceState(fm, f, outState)
        saveFragmentState(f)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        log("onFragmentViewDestroyed, fm: $fm, f: $f")
        super.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        log("onFragmentDestroyed, fm: $fm, f: $f")
        super.onFragmentDestroyed(fm, f)
        removeInjectionIfNeed(f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        log("onFragmentDetached, fm: $fm, f: $f")
        super.onFragmentDetached(fm, f)
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

    private fun isInSaveState(fragment: Fragment): Boolean =
        fragmentStateStore.getSaveState(fragment)
            .also {
                log("isInSaveState, fragment: $fragment, it: $it")
            }

    private fun saveFragmentState(fragment: Fragment) =
        fragmentStateStore.setSaveState(fragment, true)
            .also {
                log("saveFragmentState, fragment: $fragment")
            }

    private fun clearSaveState(fragment: Fragment) =
        fragmentStateStore.setSaveState(fragment, false)
            .also {
                log("clearSaveState, fragment: $fragment")
            }

    private fun log(msg: String) {
        if (DEBUG) {
            Log.d(TAG, msg)
        }
    }

    private companion object {
        private const val DEBUG: Boolean = false
        private val TAG: String = FragmentXLifecycleCallback::class.simpleName!!
    }
}