package com.github.chernovdmitriy.injectionholdercore.internal.manager

import com.github.chernovdmitriy.injectionholdercore.internal.utils.isSameClass

internal class ComponentStore {

    private val components = hashMapOf<String, Any>()

    operator fun get(key: String): Any? = components[key]

    fun add(key: String, component: Any) {
        components[key] = component
    }

    fun remove(key: String) = components.remove(key)

    @Suppress("UNCHECKED_CAST")
    fun <T> findComponent(searchedClass: Class<T>): T? =
        components.values.firstOrNull { it.isSameClass(searchedClass) } as? T
}