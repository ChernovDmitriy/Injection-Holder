package com.github.chernovdmitriy.injectionholdercore

import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback
import com.github.chernovdmitriy.injectionholdercore.registry.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholdercore.storage.ComponentStore

abstract class InjectionHolder<ApplicationType>(
    private val lifecycleCallbacksRegistry: LifecycleCallbacksRegistry<ApplicationType>
) {

    private val componentCallback: ComponentCallback by lazy {
        ComponentCallback(ComponentStore())
    }

    protected fun init(application: ApplicationType) =
        lifecycleCallbacksRegistry.registerLifecycleCallbacks(application, componentCallback)

    fun <T> getComponent(owner: ComponentOwner<T>): T = componentCallback.initOrGetComponent(owner, null)

    fun <T> getComponentOwnerLifeCycle(componentOwner: ComponentOwner<T>): ComponentOwnerLifecycle =
        componentCallback.getCustomOwnerLifecycle(componentOwner)

    fun <T> findComponent(componentClass: Class<T>): T? = componentCallback.findComponent(componentClass)
}