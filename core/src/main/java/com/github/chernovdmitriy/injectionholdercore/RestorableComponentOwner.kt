package com.github.chernovdmitriy.injectionholdercore

interface RestorableComponentOwner<SavedStateType, T> : ComponentOwner<T> {
    override fun provideComponent(): T = provideComponent(null)
    fun provideComponent(savedStateType: SavedStateType? = null): T
}