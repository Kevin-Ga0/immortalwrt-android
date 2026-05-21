package com.immortalwrt.manager.core.network

import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UbusCallIdGenerator @Inject constructor() {
    private val nextId = AtomicLong(1L)

    fun next(): Long = nextId.getAndIncrement()
}
