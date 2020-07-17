package com.github.chernovdmitriy.injectionholdercore.api

interface RestorableComponentOwner<SavedStateType, T> : ComponentOwner<T> {
    override fun provideComponent(): T = provideComponent(null)
    fun provideComponent(savedState: SavedStateType? = null): T
}