package cn.lxyhome.kotlincoroutines.entity

import java.io.Serializable
import java.util.*

/**
 *作者: lixinyang在2017/12/19创建.
 *邮箱:  lixinyang.bj@fang.com
 *用途:
 */

data class TestEntity(val map: MutableMap<String,Any?>) : Serializable {
    var _id:Int by map
    var name: String by map
    var age: Int by map
    var phoneNum: Int by map

    companion object {
        private const val serialVersionUID = 23235235464565376L
    }

    constructor(_id:Int,name:String,age:Int,phoneNum:Int) : this(HashMap()){
        this._id = _id
        this.name = name
        this.age =age
        this.phoneNum = phoneNum
    }

    override fun toString(): String {
        return "TestEntity(map=$map)"
    }


}

data class MyApplyInfo(
        var IsReadApp: String? = null,// 消息已读状态
        var ApplyId: String? = null,// 借款编号
        var ApplyDate: String? = null,// 申请日期
        var LoanMoney: String? = null,// 申请金额
        var LoanMonth: String? = null,// 申请期数
        var LoanUse: String? = null,// 贷款用途
        var LoanUseNum: String? = null,//贷款类型编号
        var DateUnit: String? = null,//日期单位 1:年2:月3：日
        var ApplyStatus: String? = null,// 申请状态
        var ApplyStatusNum: String? = null,//申请状态编号
        var LouPanID: String? = null,//楼盘ID
        var LouPanName: String? = null,//楼盘名称
        var LouPanLiveCity: String? = null,//楼盘所在城市
        var UpdateTime: String? = null//更新时间
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}