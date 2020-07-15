package com.github.chernovdmitriy.injectionholdercore.api

interface ComponentOwnerLifecycle {

    fun onCreate()

    fun onFinishDestroy()
}