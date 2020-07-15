package com.github.chernovdmitriy.injectionholderappcompat

import android.app.Application
import com.github.chernovdmitriy.injectionholderappcompat.internal.ActivityAppCompatLifecycleCallback
import com.github.chernovdmitriy.injectionholdercore.InjectionHolder
import com.github.chernovdmitriy.injectionholdercore.api.LifecycleCallbacksRegistry
import com.github.chernovdmitriy.injectionholdercore.internal.manager.ComponentManager

class InjectionHolderAppCompat private constructor() :
    InjectionHolder<Application>(AppCompatLifecycleCallbacksRegistry()) {

    companion object {
        @JvmStatic
        val instance by lazy { InjectionHolderAppCompat() }

        fun init(application: Application) = instance.init(application)
    }

    private class AppCompatLifecycleCallbacksRegistry : LifecycleCallbacksRegistry<Application> {
        override fun registerLifecycleCallbacks(app: Application, componentManager: ComponentManager) =
            app.registerActivityLifecycleCallbacks(ActivityAppCompatLifecycleCallback(componentManager))
    }
}