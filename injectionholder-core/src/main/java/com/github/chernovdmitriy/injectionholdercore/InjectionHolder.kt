package com.github.chernovdmitriy.injectionholdercore

import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback
import com.github.chernovdmitriy.injectionholdercore.registry.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholdercore.storage.ComponentsStore

abstract class InjectionHolder<ApplicationType> protected constructor(
    private val lifecycleCallbacksRegistry: LifecycleCallbacksRegistry<ApplicationType>
) {

    private val componentsStore by lazy { ComponentsStore() }

    private val componentCallback: ComponentCallback by lazy {
        ComponentCallback(componentsStore)
    }

    protected fun init(application: ApplicationType) {
        lifecycleCallbacksRegistry.registerLifecycleCallbacks(application, componentCallback)
    }

    fun removeComponent(componentClass: Class<*>) = componentsStore.remove(componentClass)

    fun <T> clearComponent(owner: ComponentOwner<T>) = componentCallback.clearComponent(owner)

    fun <T> getComponent(owner: ComponentOwner<T>): T =
        componentCallback.getComponent(owner)

    fun <T> getComponentOwnerLifeCycle(componentOwner: ComponentOwner<T>): ComponentOwnerLifecycle =
        componentCallback.getCustomOwnerLifecycle(componentOwner)

    @JvmOverloads
    fun <T> findComponent(
        componentClass: Class<T>,
        componentBuilder: (() -> T)? = null
    ): T = componentsStore.findComponent(componentClass, componentBuilder)

}