package com.github.chernovdmitriy.injectionholdercore.api

interface LifecycleCallbacksRegistry<ApplicationType> {
    fun registerLifecycleCallbacks(app: ApplicationType, componentManager: ComponentManager)
}