package cn.lxyhome.kotlincoroutines.tools

import android.util.Log
import cn.lxyhome.kotlincoroutines.MyApp

/**
 *作者: lixinyang在2017/12/22创建.
 *邮箱:  lixinyang.bj@fang.com
 *用途: 工具类
 */
private const val TAB:String = "lxy"

//<editor-fold desc="LOG">
private fun i(message: String?,bloak:()->Boolean) {
    if (bloak() || !message.isNullOrEmpty()) {
        Log.i(TAB, message)
    }
}
fun LogI(message: String?) = i(message, bloack())

fun bloack(): () -> Boolean =  {
    MyApp.openLog
}
fun Log.e(message: String) = Log.e(TAB, message)
fun Log.w(message: String) = Log.w(TAB, message)
//</editor-fold>


