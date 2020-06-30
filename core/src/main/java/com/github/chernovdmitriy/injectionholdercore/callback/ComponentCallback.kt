package com.github.chernovdmitriy.injectionholdercore.callback

import com.github.chernovdmitriy.injectionholdercore.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.ComponentOwnerLifecycle
import com.github.chernovdmitriy.injectionholdercore.RestorableComponentOwner
import com.github.chernovdmitriy.injectionholdercore.genericCastOrNull
import com.github.chernovdmitriy.injectionholdercore.storage.ComponentStore

class ComponentCallback internal constructor(private val componentStore: ComponentStore) {

    fun <SavedState, T> addInjection(
        componentOwner: ComponentOwner<T>,
        savedState: SavedState?
    ) {
        val component = initOrGetComponent(componentOwner, savedState)
        componentOwner.inject(component)
    }

    fun <T> removeInjection(componentOwner: ComponentOwner<T>) =
        componentStore.remove(componentOwner.getComponentKey())

    fun <T> getCustomOwnerLifecycle(owner: ComponentOwner<T>): ComponentOwnerLifecycle {
        return object : ComponentOwnerLifecycle {

            private var isInjected = false

            override fun onCreate() {
                if (!isInjected) {
                    addInjection(owner, null)
                    isInjected = true
                }
            }

            override fun onFinishDestroy() {
                if (isInjected) {
                    removeInjection(owner)
                    isInjected = false
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <SavedState, T> initOrGetComponent(
        componentOwner: ComponentOwner<T>,
        savedState: SavedState?
    ): T {
        val key = componentOwner.getComponentKey()
        return componentStore[key] as? T
            ?: initComponent(componentOwner, savedState).also { componentStore.add(key, it as Any) }
    }

    private fun <SavedState, T> initComponent(
        componentOwner: ComponentOwner<T>,
        savedState: SavedState?
    ): T {
        return genericCastOrNull<RestorableComponentOwner<SavedState, T>>(componentOwner)
            ?.provideComponent(savedState)
            ?: componentOwner.provideComponent()
    }

    fun <T> findComponent(componentClass: Class<T>): T? = componentStore.findComponent(componentClass)
}