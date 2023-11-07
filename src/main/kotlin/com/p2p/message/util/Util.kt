package com.p2p.message.util

import kotlin.reflect.full.companionObject

fun <T : Any> fetchClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}