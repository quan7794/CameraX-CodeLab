package com.samsung.android.plugin.tv.v3.edgeBlending.external

import android.util.Log
import com.samsung.android.plugin.tv.v3.edgeBlending.external.EbLogger


class EbLoggerImpl : EbLogger {
    override fun log(level: Int, tag: String?, functionName: String, message: String) {
//        tag?.let {
//            when (level) {
//                Log.VERBOSE -> Log.v(it, functionName, message)
//                Log.INFO -> TVPLog.i(it, functionName, message)
//                Log.DEBUG -> TVPLog.d(it, functionName, message)
//                Log.WARN -> TVPLog.w(it, functionName, message)
//                Log.ERROR -> TVPLog.e(it, functionName, message)
//            }
//        }
    }

    override fun d(className: String, functionName: String, msg: String?) {
       // TVPLog.d(className, functionName, msg)
    }

    override fun i(className: String, functionName: String, msg: String?) {
        //TVPLog.i(className, functionName, msg)
    }

    override fun e(className: String, functionName: String, msg: String?) {
        //TVPLog.e(className, functionName, msg)
    }

    override fun w(className: String, functionName: String, msg: String?) {
       // TVPLog.w(className, functionName, msg)
    }

    override fun v(className: String, functionName: String, msg: String?) {
        //TVPLog.v(className, functionName, msg)
    }
}