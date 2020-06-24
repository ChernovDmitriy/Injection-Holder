package com.github.chernovdmitriy.injectionholderappcompat

import android.app.Application
import com.github.chernovdmitriy.injectionholdercore.InjectionHolder
import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback
import com.github.chernovdmitriy.injectionholdercore.registry.LifecycleCallbacksRegistry

class InjectionHolderAppCompat private constructor() :
    InjectionHolder<Application>(AppCompatLifecycleCallbacksRegistry()) {

    companion object {
        @JvmStatic
        val instance by lazy { InjectionHolderAppCompat() }

        fun init(application: Application) = instance.init(application)
    }

    private class AppCompatLifecycleCallbacksRegistry : LifecycleCallbacksRegistry<Application> {
        override fun registerLifecycleCallbacks(
            app: Application,
            componentCallback: ComponentCallback
        ) =
            app.registerActivityLifecycleCallbacks(
                ActivityAppCompatLifecycleCallback(
                    componentCallback
                )
            )
    }
}