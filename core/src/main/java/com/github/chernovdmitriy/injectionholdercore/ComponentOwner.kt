package com.github.chernovdmitriy.injectionholdercore

interface ComponentOwner<T> {

    fun provideComponent(): T

    fun getComponentKey(): String = javaClass.toString()

    fun inject(t: T)
}