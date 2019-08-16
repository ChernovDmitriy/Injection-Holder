package com.github.chernovdmitriy.injectionholdercore

import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback
import com.github.chernovdmitriy.injectionholdercore.registry.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholdercore.storage.ComponentsStore

abstract class InjectionHolder<ApplicationType>(
    private val lifecycleCallbacksRegistry: LifecycleCallbacksRegistry<ApplicationType>
) {

    private val componentsStore by lazy { ComponentsStore() }

    private val componentCallback: ComponentCallback by lazy { ComponentCallback(componentsStore) }

    protected fun init(application: ApplicationType) =
        lifecycleCallbacksRegistry.registerLifecycleCallbacks(application, componentCallback)

    fun removeComponent(componentClass: Class<*>) = componentCallback.removeComponent(componentClass)

    fun <T> removeComponent(owner: ComponentOwner<T>) = componentCallback.removeComponent(owner)

    fun <T> getComponent(owner: ComponentOwner<T>): T = componentCallback.initOrGetComponent(owner)

    fun <T> getComponentOwnerLifeCycle(componentOwner: ComponentOwner<T>): ComponentOwnerLifecycle =
        componentCallback.getCustomOwnerLifecycle(componentOwner)

    @JvmOverloads
    fun <T> findComponent(
        componentClass: Class<T>,
        componentBuilder: (() -> T)? = null
    ): T = componentsStore.findComponent(componentClass, componentBuilder)

}