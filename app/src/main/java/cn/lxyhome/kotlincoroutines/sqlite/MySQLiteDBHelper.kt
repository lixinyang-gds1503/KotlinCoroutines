package cn.lxyhome.kotlincoroutines.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cn.lxyhome.kotlincoroutines.tools.LogI
import org.jetbrains.anko.db.*

/**
 *作者: lixinyang在2017/12/20创建.
 *邮箱:  lixinyang.bj@fang.com
 *用途: 创建数据库
 */
class MySQLiteDBHelper(context: Context,newVersion:Int =1) : ManagedSQLiteOpenHelper(context, "mydatabases.db",version = newVersion) {

    companion object {
        private var mdb: MySQLiteDBHelper? = null
        @Synchronized
        fun newInstants(content: Context,newVersion: Int): MySQLiteDBHelper {
            if (mdb == null) {
                mdb = MySQLiteDBHelper(content.applicationContext,newVersion)
            }
            return mdb!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable("tongxunlu", true,
                "_id" to INTEGER + PRIMARY_KEY,
                "name" to TEXT,
                "phoneNum" to INTEGER,
                "age" to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion==2) {
            addTableColumns(db,"tongxunlu","address")
        }
    }

    private fun isColumnExist(db:SQLiteDatabase?, tableName:String?, columnName:String?):Boolean {
        var ok = false
        val sql = ("select count(1) as c from sqlite_master where type ='table' and name ='"
                + tableName?.trim({ it <= ' ' }) + "' and sql like '%" + columnName?.trim({ it <= ' ' }) + "%'")
        val cursor = db?.rawQuery(sql, null)
        cursor?.moveToNext()?.let {
            val int = cursor.getInt(0)
            if (int >0) {
                ok = true
                return ok
            }
        }
        cursor?.close()
        return  ok
    }
    private fun addTableColumns(db: SQLiteDatabase?, tableName: String?, columns:String?) {
        if (!isColumnExist(db, tableName,columns)) {
            val sql = "alter table $tableName add $columns varchar"
            try {
                db?.execSQL(sql)
            } catch (e: Exception) {
                LogI(e.toString())
            }
        }
    }
}