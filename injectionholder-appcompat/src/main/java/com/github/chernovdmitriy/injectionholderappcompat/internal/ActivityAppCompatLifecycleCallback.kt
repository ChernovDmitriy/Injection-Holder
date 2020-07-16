package com.github.chernovdmitriy.injectionholderappcompat.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.chernovdmitriy.injectionholdercore.api.ComponentManager
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner

internal class ActivityAppCompatLifecycleCallback(
    private val componentManager: ComponentManager
) : Application.ActivityLifecycleCallbacks {

    private companion object {
        const val IS_FIRST_LAUNCH = "com.github.chernovdmitriy.injectionholderappcompat.internal.IS_FIRST_LAUNCH"
    }

    private fun isFirstLaunch(outState: Bundle?): Boolean =
        outState?.getBoolean(IS_FIRST_LAUNCH, true) ?: true

    private fun setFirstLaunch(outState: Bundle?, isFirstLaunch: Boolean) {
        outState?.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch)
    }

    private fun addInjectionIfNeed(activity: Activity, outState: Bundle?) {
        if (activity is ComponentOwner<*>) {
            if (isFirstLaunch(outState)) {
                componentManager.removeInjection(activity)
            }
            componentManager.addInjection(activity, outState)
        }

        (activity as? AppCompatActivity)
            ?.supportFragmentManager
            ?.registerFragmentLifecycleCallbacks(
                FragmentAppCompatLifecycleCallback(componentManager, FragmentStateStore.instance),
                true
            )
    }

    private fun removeInjectionIfNeed(activity: Activity) {
        if (activity is ComponentOwner<*> && activity.isFinishing) {
            componentManager.removeInjection(activity)
        }
    }

    override fun onActivityCreated(activity: Activity, outState: Bundle?) {
        addInjectionIfNeed(activity, outState)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
        removeInjectionIfNeed(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        setFirstLaunch(outState, isFirstLaunch = false)
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}