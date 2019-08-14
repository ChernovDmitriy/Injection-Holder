package com.github.chernovdmitriy.injectionholderx

import android.app.Application
import com.github.chernovdmitriy.injectionholdercore.InjectionHolder
import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback
import com.github.chernovdmitriy.injectionholdercore.registry.LifecycleCallbacksRegistry

class InjectionHolderX private constructor() : InjectionHolder<Application>(AndroidXLifecycleCallbacksRegistry()) {

    companion object {
        @JvmStatic
        val instance by lazy { InjectionHolderX() }

        fun init(application: Application) = instance.init(application)
    }

    private class AndroidXLifecycleCallbacksRegistry : LifecycleCallbacksRegistry<Application> {
        override fun registerLifecycleCallbacks(app: Application, componentCallback: ComponentCallback) =
            app.registerActivityLifecycleCallbacks(ActivityXLifecycleCallback(componentCallback))
    }

}