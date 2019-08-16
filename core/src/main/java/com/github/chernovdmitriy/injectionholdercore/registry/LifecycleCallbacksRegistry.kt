package com.github.chernovdmitriy.injectionholdercore.registry

import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback

interface LifecycleCallbacksRegistry<ApplicationType> {
    fun registerLifecycleCallbacks(app: ApplicationType, componentCallback: ComponentCallback)
}