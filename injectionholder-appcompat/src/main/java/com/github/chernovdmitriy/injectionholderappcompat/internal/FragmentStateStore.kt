package com.github.chernovdmitriy.injectionholderappcompat.internal

import android.support.v4.app.Fragment
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner

internal class FragmentStateStore private constructor() {

    companion object {
        val instance by lazy { FragmentStateStore() }
    }

    private val stateMap: MutableMap<String, Boolean> = hashMapOf()

    fun setSaveState(f: Fragment, isInSaveState: Boolean) {
        f.getComponentKey()?.let { stateMap[it] = isInSaveState }
    }

    fun getSaveState(f: Fragment): Boolean {
        return f.getComponentKey()?.let { stateMap[it] } ?: false
    }

    private fun Fragment.getComponentKey(): String? =
        (this as? ComponentOwner<*>)?.getComponentKey()
}