package com.github.chernovdmitriy.injectionholdercore

class ComponentNotFoundException(key: String) : Throwable("Component of the $key type was not found")