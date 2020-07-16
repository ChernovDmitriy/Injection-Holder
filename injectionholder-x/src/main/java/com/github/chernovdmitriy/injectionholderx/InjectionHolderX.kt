package com.github.chernovdmitriy.injectionholderx

import android.app.Application
import com.github.chernovdmitriy.injectionholdercore.InjectionHolder
import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholderx.internal.ActivityXLifecycleCallback

class InjectionHolderX private constructor() : InjectionHolder<Application>(AndroidXLifecycleCallbacksRegistry()) {

    companion object {
        @JvmStatic
        val instance by lazy { InjectionHolderX() }

        fun init(application: Application) = instance.init(application)
    }

    private class AndroidXLifecycleCallbacksRegistry : LifecycleCallbacksRegistry<Application> {
        override fun registerLifecycleCallbacks(app: Application, componentManager: ComponentManager) =
            app.registerActivityLifecycleCallbacks(ActivityXLifecycleCallback(componentManager))
    }
}