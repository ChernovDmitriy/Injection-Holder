package com.github.chernovdmitriy.injectionholdercore.storage

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

    private fun Any.isSameClass(otherClass: Class<*>): Boolean =
        otherClass.isAssignableFrom(javaClass) || javaClass.isAssignableFrom(otherClass)
}