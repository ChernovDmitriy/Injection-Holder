package com.github.chernovdmitriy.injectionholdercore

import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwnerLifecycle
import com.github.chernovdmitriy.injectionholdercore.api.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholdercore.internal.manager.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.internal.manager.ComponentStore

abstract class InjectionHolder<ApplicationType>(
    private val lifecycleCallbacksRegistry: LifecycleCallbacksRegistry<ApplicationType>
) {

    private val componentManager: ComponentManager by lazy { ComponentManager(ComponentStore()) }

    protected fun init(application: ApplicationType) =
        lifecycleCallbacksRegistry.registerLifecycleCallbacks(application, componentManager)

    fun <T> getComponent(owner: ComponentOwner<T>): T = componentManager.initOrGetComponent(owner, null)

    fun <T> getComponentOwnerLifeCycle(componentOwner: ComponentOwner<T>): ComponentOwnerLifecycle =
        componentManager.getCustomOwnerLifecycle(componentOwner)

    fun <T> findComponent(componentClass: Class<T>): T? = componentManager.findComponent(componentClass)
}