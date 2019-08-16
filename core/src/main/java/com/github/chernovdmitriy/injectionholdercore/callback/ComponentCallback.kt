package com.github.chernovdmitriy.injectionholdercore.callback

import com.github.chernovdmitriy.injectionholdercore.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.ComponentOwnerLifecycle
import com.github.chernovdmitriy.injectionholdercore.storage.ComponentsStore

class ComponentCallback internal constructor(private val componentsStore: ComponentsStore) {

    fun <T> addInjection(componentOwner: ComponentOwner<T>) {
        val component = initOrGetComponent(componentOwner)
        componentOwner.inject(component)
    }

    fun <T> removeInjection(componentOwner: ComponentOwner<T>) =
        componentsStore.remove(componentOwner.getComponentKey())

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

    fun <T> removeComponent(componentClass: Class<T>) = componentsStore.remove(componentClass)

    fun <T> removeComponent(componentOwner: ComponentOwner<T>) =
        componentsStore.remove(componentOwner.getComponentKey())

    @Suppress("UNCHECKED_CAST")
    fun <T> initOrGetComponent(owner: ComponentOwner<T>): T {
        val ownerKey = owner.getComponentKey()

        return if (componentsStore.isExist(ownerKey)) {
            componentsStore[ownerKey] as T
        } else {
            owner.provideComponent().also { newComponent ->
                componentsStore.add(ownerKey, newComponent as Any)
            }
        }
    }

}