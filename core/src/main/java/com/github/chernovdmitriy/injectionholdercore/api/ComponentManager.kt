package com.github.chernovdmitriy.injectionholdercore.api

interface ComponentManager {

    fun <T> getComponentOwnerLifecycle(owner: ComponentOwner<T>): ComponentOwnerLifecycle

    fun <T> findComponent(componentClass: Class<T>): T?

    fun <SavedState, T> addInjection(componentOwner: ComponentOwner<T>, savedState: SavedState?)

    fun <T> removeInjection(componentOwner: ComponentOwner<T>)
}