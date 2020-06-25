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
        val ownerKey = componentOwner.getComponentKey()

        @Suppress("UNCHECKED_CAST")
        val component =
            when {
                componentStore.isExist(ownerKey) -> componentStore[ownerKey] as T
                else -> {
                    (genericCastOrNull<RestorableComponentOwner<SavedState, T>>(componentOwner)
                        ?.provideComponent(savedState)
                        ?: componentOwner.provideComponent())
                        .also { componentStore.add(ownerKey, it as Any) }
                }
            }

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
    fun <T> initOrGetComponent(owner: ComponentOwner<T>): T {
        val ownerKey = owner.getComponentKey()

        return if (componentStore.isExist(ownerKey)) {
            componentStore[ownerKey] as T
        } else {
            (genericCastOrNull<RestorableComponentOwner<Any?, T>>(owner)
                ?.provideComponent(null)
                ?: owner.provideComponent())
                .also { componentStore.add(ownerKey, it as Any) }
        }
    }

    fun <T> findComponent(
        componentClass: Class<T>
    ): T? = componentStore.findComponent(componentClass)
}