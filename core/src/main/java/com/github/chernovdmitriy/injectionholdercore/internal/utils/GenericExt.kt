package com.github.chernovdmitriy.injectionholdercore.internal.utils

internal inline fun <reified T> genericCastOrNull(any: Any): T? = any as? T

internal fun Any.isSameClass(otherClass: Class<*>): Boolean =
    otherClass.isAssignableFrom(javaClass) || javaClass.isAssignableFrom(otherClass)