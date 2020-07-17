package com.github.chernovdmitriy.injectionholderappcompat

import android.app.Application
import com.github.chernovdmitriy.injectionholderappcompat.internal.ActivityAppCompatLifecycleCallback
import com.github.chernovdmitriy.injectionholdercore.InjectionHolder
import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.LifecycleCallbacksRegistry

object InjectionHolderAppCompat : InjectionHolder<Application>(AppCompatLifecycleCallbacksRegistry()) {

    private class AppCompatLifecycleCallbacksRegistry : LifecycleCallbacksRegistry<Application> {
        override fun registerLifecycleCallbacks(app: Application, componentManager: ComponentManager) =
            app.registerActivityLifecycleCallbacks(ActivityAppCompatLifecycleCallback(componentManager))
    }
}