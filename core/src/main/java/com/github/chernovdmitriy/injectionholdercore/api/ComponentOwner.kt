package com.github.chernovdmitriy.injectionholdercore.api

interface ComponentOwner<T> {

    fun provideComponent(): T

    fun getComponentKey(): String = javaClass.toString()

    fun inject(t: T)
}