package com.github.chernovdmitriy.injectionholdercore.callback

import com.github.chernovdmitriy.injectionholdercore.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.ComponentOwnerLifecycle
import com.github.chernovdmitriy.injectionholdercore.storage.ComponentStore

class ComponentCallback internal constructor(private val componentStore: ComponentStore) {

    fun <T> addInjection(componentOwner: ComponentOwner<T>) {
        val component = initOrGetComponent(componentOwner)
        componentOwner.inject(component)
    }

    fun <T> removeInjection(componentOwner: ComponentOwner<T>) =
        componentStore.remove(componentOwner.getComponentKey())

    fun <T> getCustomOwnerLifecycle(owner: ComponentOwner<T>): ComponentOwnerLifecycle {
        return object : ComponentOwnerLifecycle {

            private var isInjected = false

            override fun onCreate() {
                if (!isInjected) {
                    addInjection(owner)
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

    fun addOwnerlessComponent(component: Any) = componentStore.addOwnerLessComponent(component)

    fun <T> removeComponent(componentClass: Class<T>) = componentStore.remove(componentClass)

    fun <T> removeComponent(componentOwner: ComponentOwner<T>) =
        componentStore.remove(componentOwner.getComponentKey())

    @Suppress("UNCHECKED_CAST")
    fun <T> initOrGetComponent(owner: ComponentOwner<T>): T {
        val ownerKey = owner.getComponentKey()

        return if (componentStore.isExist(ownerKey)) {
            componentStore[ownerKey] as T
        } else {
            owner.provideComponent().also { newComponent ->
                componentStore.add(ownerKey, newComponent as Any)
            }
        }
    }

    fun <T> findComponent(
        componentClass: Class<T>,
        componentBuilder: (() -> T)? = null
    ): T = componentStore.findComponent(componentClass, componentBuilder)
}