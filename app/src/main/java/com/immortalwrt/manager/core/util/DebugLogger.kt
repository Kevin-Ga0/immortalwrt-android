package com.immortalwrt.manager.core.util

import com.immortalwrt.manager.BuildConfig

object DebugLogger {
    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) android.util.Log.d(tag, msg)
    }

    fun e(tag: String, msg: String, t: Throwable? = null) {
        if (BuildConfig.DEBUG) android.util.Log.e(tag, msg, t)
    }
}
