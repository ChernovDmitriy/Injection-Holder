package com.github.chernovdmitriy.injectionholderx

import android.app.Application
import com.github.chernovdmitriy.injectionholdercore.InjectionHolder
import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholderx.internal.ActivityXLifecycleCallback

object InjectionHolderX : InjectionHolder<Application>(AndroidXLifecycleCallbacksRegistry()) {

    private class AndroidXLifecycleCallbacksRegistry : LifecycleCallbacksRegistry<Application> {
        override fun registerLifecycleCallbacks(app: Application, componentManager: ComponentManager) =
            app.registerActivityLifecycleCallbacks(ActivityXLifecycleCallback(componentManager))
    }
}