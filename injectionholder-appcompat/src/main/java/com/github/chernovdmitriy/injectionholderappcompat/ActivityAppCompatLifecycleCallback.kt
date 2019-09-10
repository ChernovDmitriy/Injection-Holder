package com.github.chernovdmitriy.injectionholderappcompat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.chernovdmitriy.injectionholdercore.ComponentOwner
import com.github.chernovdmitriy.injectionholdercore.callback.ComponentCallback

internal class ActivityAppCompatLifecycleCallback(
    private val componentCallback: ComponentCallback
) : Application.ActivityLifecycleCallbacks {

    private companion object {
        const val IS_FIRST_LAUNCH = "com.github.chernovdmitriy.injectionholderappcompat.IS_FIRST_LAUNCH"
    }

    private fun isFirstLaunch(outState: Bundle?): Boolean =
        outState?.getBoolean(IS_FIRST_LAUNCH, true) ?: true

    private fun setFirstLaunch(outState: Bundle?, isFirstLaunch: Boolean) {
        outState?.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch)
    }

    private fun addInjectionIfNeed(activity: Activity, outState: Bundle?) {
        if (activity is ComponentOwner<*>) {
            if (isFirstLaunch(outState)) {
                componentCallback.removeInjection(activity)
            }
            componentCallback.addInjection(activity)
        }

        (activity as? AppCompatActivity)
            ?.supportFragmentManager
            ?.registerFragmentLifecycleCallbacks(
                FragmentAppCompatLifecycleCallback(componentCallback, FragmentStateStore.instance),
                true
            )
    }

    private fun removeInjectionIfNeed(activity: Activity) {
        if (activity is ComponentOwner<*> && activity.isFinishing) {
            componentCallback.removeInjection(activity)
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