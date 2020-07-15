package com.github.chernovdmitriy.injectionholdercore.api

import com.github.chernovdmitriy.injectionholdercore.internal.manager.ComponentManager

interface LifecycleCallbacksRegistry<ApplicationType> {
    fun registerLifecycleCallbacks(app: ApplicationType, componentManager: ComponentManager)
}