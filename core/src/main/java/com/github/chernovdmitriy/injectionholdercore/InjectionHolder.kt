package com.github.chernovdmitriy.injectionholdercore

import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwnerLifecycle
import com.github.chernovdmitriy.injectionholdercore.api.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholdercore.internal.manager.ComponentManagerImpl

abstract class InjectionHolder<ApplicationType>(
    private val lifecycleCallbacksRegistry: LifecycleCallbacksRegistry<ApplicationType>
) {

    private val componentManager: ComponentManager by lazy { ComponentManagerImpl() }

    fun init(application: ApplicationType) =
        lifecycleCallbacksRegistry.registerLifecycleCallbacks(application, componentManager)

    fun <T> getComponentOwnerLifeCycle(componentOwner: ComponentOwner<T>): ComponentOwnerLifecycle =
        componentManager.getComponentOwnerLifecycle(componentOwner)

    fun <T> findComponent(componentClass: Class<T>): T? = componentManager.findComponent(componentClass)
}