package cn.lxyhome.kotlincoroutines.sqlite

import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SelectQueryBuilder

/**
 *作者: lixinyang在2017/12/20创建.
 *邮箱:  lixinyang.bj@fang.com
 *用途:
 */

fun <T : Any> SelectQueryBuilder.parseList(
        parser: (Map<String, Any?>) -> T): List<T> = parseList(object : MapRowParser<T> { override fun parseRow(columns: Map<String, Any?>): T = parser(columns) })
