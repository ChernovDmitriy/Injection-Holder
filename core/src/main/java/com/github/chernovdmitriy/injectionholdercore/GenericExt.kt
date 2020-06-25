package com.github.chernovdmitriy.injectionholdercore

internal inline fun <reified T> genericCastOrNull(any: Any): T? = any as? T