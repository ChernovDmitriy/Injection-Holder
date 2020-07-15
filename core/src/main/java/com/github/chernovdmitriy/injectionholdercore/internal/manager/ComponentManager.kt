package com.github.chernovdmitriy.injectionholdercore.internal.manager

import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwnerLifecycle
import com.github.chernovdmitriy.injectionholdercore.api.RestorableComponentOwner
import com.github.chernovdmitriy.injectionholdercore.internal.utils.genericCastOrNull

class ComponentManager internal constructor(private val componentStore: ComponentStore) {

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