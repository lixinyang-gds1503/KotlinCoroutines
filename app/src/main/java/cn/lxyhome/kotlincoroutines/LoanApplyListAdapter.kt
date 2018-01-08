package cn.lxyhome.kotlincoroutines

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 作者: lixinyang在2017/12/20创建.
 * 邮箱:  lixinyang.bj@fang.com
 * 用途:
 */

class LoanApplyListAdapter(private val context: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LinearLayout.inflate(context, R.layout.item, null)
            convertView!!.findViewById<TextView>(R.id.age)
            holder = ViewHolder()
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        return convertView
    }

    internal inner class ViewHolder {
        var tv_applyID: TextView? = null
        var tv_applyDate: TextView? = null
        var tv_applyStatus: TextView? = null
        var tv_loanUse: TextView? = null
        var tv_loanMoney: TextView? = null
        var tv_loan_date: TextView? = null
        var iv_isRead: ImageView? = null
    }
}