package com.github.chernovdmitriy.injectionholdercore

internal class ComponentNotFoundException(key: String) : Throwable("Component of the $key type was not found")