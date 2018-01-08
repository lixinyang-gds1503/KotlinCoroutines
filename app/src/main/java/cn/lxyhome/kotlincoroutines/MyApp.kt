package cn.lxyhome.kotlincoroutines

import android.app.Activity
import android.app.Application
import android.os.Bundle
import cn.lxyhome.kotlincoroutines.sqlite.MySQLiteDBHelper
import cn.lxyhome.kotlincoroutines.tools.LogI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 *作者: lixinyang在2017/12/20创建.
 *邮箱:  lixinyang.bj@fang.com
 *用途:
 */
class MyApp : Application(),Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        val message = activity?.javaClass?.simpleName
        LogI(message)
    }

    companion object {
        @JvmStatic
        lateinit var mApp: MyApp
        lateinit var mDB:MySQLiteDBHelper
        var openLog:Boolean = true
        val DBVERSION:Int = 2
    }

    override fun onCreate() {
        super.onCreate()
        mApp = applicationContext as MyApp
        async {
            bg {
                mDB = MySQLiteDBHelper.newInstants(mApp, DBVERSION)
                mDB.writableDatabase
            }
        }
       // MySQLiteDBHelper(applicationContext)
        this.registerActivityLifecycleCallbacks(this)
    }
}