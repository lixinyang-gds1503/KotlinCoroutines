package cn.lxyhome.kotlincoroutines.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cn.lxyhome.kotlincoroutines.MyApp
import cn.lxyhome.kotlincoroutines.R
import cn.lxyhome.kotlincoroutines.entity.TestEntity
import cn.lxyhome.kotlincoroutines.tools.LogI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.update_dialog.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.alert
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onItemLongClick
import org.jetbrains.anko.toast
import kotlin.coroutines.experimental.buildSequence

/**
 * 用协程配合数据库走一个
 */
class RecordActivity : AppCompatActivity() {
    private var _id: Int = 0
    private var name: String = ""
    private var age: Int = 0
    private var phoneNum: Int = 0
    private val tableName = "tongxunlu"
    private val dialoglist = arrayListOf<String>("修改", "删除", "取消")

    private val arraylist = arrayListOf<TestEntity>()
    private val listadapter = ListViewadapter()

    private inner class ListViewadapter(val list: ArrayList<TestEntity> = arraylist) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            val hoderView: Hodleview
            if (view == null) {
                hoderView = Hodleview()
                view = this@RecordActivity.layoutInflater.inflate(R.layout.item, null)
                hoderView.age = view.age
                hoderView.name = view.name
                hoderView.phoneNum = view.phoneNum
                view!!.tag = hoderView
            } else {
                hoderView = view.tag as Hodleview
            }
            hoderView.age.text = arraylist[position].age.toString()
            hoderView.name.text = arraylist[position].name
            hoderView.phoneNum.text = arraylist[position].phoneNum.toString()
            return view
        }

        override fun getItem(position: Int): Any = arraylist[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int = arraylist.size


        private inner class Hodleview {
            lateinit var name: TextView
            lateinit var age: TextView
            lateinit var phoneNum: TextView
        }

    }

    private inner class DialogAdapter(val list: ArrayList<String> = dialoglist) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var viewdialogitem = convertView
            val hoderView: Hodleview
            if (viewdialogitem == null) {
                hoderView = Hodleview()
                viewdialogitem = this@RecordActivity.layoutInflater.inflate(R.layout.item_dialog, null)
                hoderView.message = viewdialogitem.find(R.id.message)
                viewdialogitem!!.tag = hoderView
            } else {
                hoderView = viewdialogitem.tag as Hodleview
            }
            hoderView.message.text = list[position]
            return viewdialogitem
        }

        override fun getItem(position: Int): Any = list[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int = list.size


        private inner class Hodleview {
            lateinit var message: TextView
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val toList = startCoroutines().take(8).toList()
        Log.i("lxy",toList.toString())*/
        setListView()
        startReadDatabase()
        ButtonsetOnClick()

    }

    private fun startReadDatabase() {
        async(UI) {
            val asReference = this.asReference()//软引用

            //在工作线程中
            val bg = bg {
                read()
            }
            val elements = bg.await()
            UI.dispatch(UI, Runnable {
                arraylist.clear()
                arraylist.addAll(elements)
                if (arraylist.size > 0) {
                    arraylist.last().let {
                        _id = it._id
                    }
                }
            })
        }
    }


    private fun read(): ArrayList<TestEntity> {
        return MyApp.mDB.use {
            val list: List<TestEntity>?
            try {
                //list = select("tongxunlu").whereArgs("_id >= 0").parseList (classParser())
                val parser: MapRowParser<TestEntity> = object : MapRowParser<TestEntity> {
                    override fun parseRow(columns: Map<String, Any?>): TestEntity = TestEntity(HashMap(columns))
                }

                list = select(tableName).whereArgs("_id >= ${0}").parseList(parser)
                val testEntity = select(tableName).whereArgs("").orderBy("_id", SqlOrderDirection.DESC).limit(1).parseOpt(parser)
                LogI(testEntity.toString())
                val arrlist = ArrayList<TestEntity>()
                arrlist.addAll(list)
                arrlist
            } catch (e: Exception) {
                e.printStackTrace()
                return@use ArrayList<TestEntity>()
            }
        }
    }


    private fun setListView() {
        listview.adapter = listadapter
        listview.onItemLongClick { _, _, position, _ ->
            alert {
                title = "你想要..."
                items(dialoglist){ dialog, index ->
                    when (index) {
                        0 -> {
                            updateDatabeses(arraylist[position]){
                                updateDialog(it)
                            }
                        }
                        1 -> {
                            dialog.dismiss()
                            deleteDialog(position)
                        }
                        2 -> {
                            dialog.dismiss()
                        }
                    }
                }
            }.show()

            /* AlertDialog.Builder(this@MainActivity).setTitle("你想要...").setAdapter(DialogAdapter()) { dialog, which ->
                 when (which) {
                     0 -> {
                         updateDatabeses(arraylist[position]){
                             updateDialog(it)
                         }
                     }
                     1 -> {
                         dialog.dismiss()
                         deleteDialog(position)
                     }
                     2 -> {
                         dialog.dismiss()
                     }
                 }

             }.create().show()*/

        }
    }

    private inline fun updateDatabeses(data: TestEntity, body:(data:TestEntity)->Unit) { //inline 是如何使用的
        body(data)
    }

    private  fun updateDialog(data: TestEntity) {
        val dialogview = this@RecordActivity.layoutInflater.inflate(R.layout.update_dialog, null)
        dialogview.dialog_et_name.setText(data.name)
        dialogview.dialog_et_age.setText(data.age.toString())
        dialogview.dialog_et_phone.setText(data.phoneNum.toString())
        AlertDialog.Builder(this@RecordActivity).setTitle("修改").setView(dialogview as View).setNegativeButton("确定"){
            dialog, _ ->

            try {
                data.name = dialogview.dialog_et_name.text.toString()
                val toInt = dialogview.dialog_et_age.text.toString().toInt()
                data.age = toInt
                val toInt1 = dialogview.dialog_et_phone.text.toString().toInt()
                data.phoneNum = toInt1

                MakeDadebase(data)
                listadapter.notifyDataSetChanged()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }.create().show()

    }

    private fun MakeDadebase(data: TestEntity) {
        async {
            val map = hashMapOf("name" to data.name, "age" to data.age, "phoneNum" to data.phoneNum)
            val argsmap = hashMapOf("_id" to data._id)
            val bg = bg {
                MyApp.mDB.use {
                    try {
                        update(tableName, *map.map { Pair(it.key, it.value) }.toTypedArray())
                                .whereArgs("_id={_id}", *argsmap.map { Pair(it.key, it.value) }.toTypedArray()).exec()// 重点
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    private fun deleteDialog(position: Int) {
        AlertDialog.Builder(this@RecordActivity).setTitle("是否要删除？").setNegativeButton("删除") { dialog, _ ->
            dialog.dismiss()
            val testEntity = arraylist.removeAt(position)
            listadapter.notifyDataSetChanged()
            deletedatabes(testEntity)
        }.setPositiveButton("取消") { dialog, _ ->
            dialog.dismiss()
        }.create().show()
    }

    /**
     * @param testEntity  This is deleted element of TestEntity from table
     */
    private fun deletedatabes(testEntity: TestEntity) {

        async {
            bg {
                MyApp.mDB.use {
                    try {
                        val args = arrayOf<String>(testEntity._id.toString())
                        delete(tableName, "_id=?", args)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun ButtonsetOnClick() {
        btn_commint.onClick {
            //todo
            if (HandData()) {
                startwriteDatabase()
            }
        }
    }

    private fun startwriteDatabase() {
        async {
            insertData()
        }
    }

    private suspend fun insertData() {
        MyApp.mDB.use {
            val map = hashMapOf("name" to name, "phoneNum" to phoneNum, "age" to age)
            insert(tableName, *map.map { Pair(it.key, it.value) }.toTypedArray())
        }
    }

    private fun HandData(): Boolean {
        try {
            name = et_name.text.toString()
            age = et_age.text.toString().let {
                if (it.isEmpty()) {
                    0
                } else {
                    Integer.parseInt(it)
                }
            }
            phoneNum = et_phone.text.toString().let {
                if (it.isNotEmpty()) Integer.parseInt(it) else 0
            }
            if (age == 0 && phoneNum == 0) {
                return false
            }
            val entity = TestEntity(_id, name, age, phoneNum)

            et_name.setText("")
            et_age.setText("")
            et_phone.setText("")

            arraylist.add(entity)
            listadapter.notifyDataSetChanged()
            _id++
        } catch (e: NumberFormatException) {
            toast("内容填写错误")
            return false
        }
        return true
    }


    private fun startCoroutines(): Sequence<Int> {
        return buildSequence<Int> {
            var a = 0
            var b = 1

            while (true) {
                yield(a + b)
                val tmp = a + b
                a = b
                b = tmp
            }
        }
    }

}
