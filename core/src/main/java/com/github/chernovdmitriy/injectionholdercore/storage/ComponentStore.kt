package com.github.chernovdmitriy.injectionholdercore.storage

import com.github.chernovdmitriy.injectionholdercore.ComponentNotFoundException

internal class ComponentStore {

    private val components = hashMapOf<String, Any>()
    private val ownerLessComponents = hashSetOf<Any>()

    fun addOwnerLessComponent(component: Any) {
        ownerLessComponents.add(component)
    }

    fun add(key: String, component: Any) {
        components[key] = component
        ownerLessComponents.remove(component)
    }

    operator fun get(key: String): Any = components[key] ?: throw ComponentNotFoundException(key)

    fun remove(key: String) {
        val component = components.remove(key)
        component?.let { ownerLessComponents.remove(it) }
    }

    fun remove(componentClass: Class<*>): Boolean {
        var searchedComponent: Any? = null

        for (component in components.values) {
            if (component.isSameClass(componentClass)) {
                searchedComponent = component
                break
            }
        }

        searchedComponent?.let {
            ownerLessComponents.remove(it)
            return components.values.remove(it)
        }

        return false
    }

    fun isExist(key: String) = components.containsKey(key)

    @Suppress("UNCHECKED_CAST")
    fun <T> findComponent(
        searchedClass: Class<T>,
        componentBuilder: (() -> T)?
    ): T {

        components.values.forEach { component ->
            if (component.isSameClass(searchedClass)) {
                return component as T
            }
        }

        //else
        ownerLessComponents.forEach { component ->
            if (component.isSameClass(searchedClass)) {
                return component as T
            }
        }

        //else
        componentBuilder?.invoke()?.let { newComponent -> return newComponent }

        //else
        throw ComponentNotFoundException(searchedClass.simpleName)
    }

    private fun Any.isSameClass(otherClass: Class<*>): Boolean =
        otherClass.isAssignableFrom(javaClass) || javaClass.isAssignableFrom(otherClass)
}