package com.fizzer.base.lib.utils

import android.annotation.SuppressLint
import android.util.Log
import com.fizzer.base.lib.log.LogUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.regex.Pattern


/**
 * @Author Fizzer503@gmail.com
 * @Date 2024/4/26
 * @Desc ID身份证的各种校验工具类
 *
 *     /*********************************** 身份证验证开始 ****************************************/
 *     /**
 *      * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
 *      * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
 *      * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
 *      * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
 *      * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
 *      * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
 *      * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
 *      * （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 *      */
 */
@SuppressLint("SimpleDateFormat")
class IDCardUtils private constructor() {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { IDCardUtils() }
    }

    private val codeArr = arrayListOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
    private val wi = arrayListOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    private val gc by lazy { GregorianCalendar() }
    private val sdf by lazy { SimpleDateFormat("yyyy-MM-dd") }

    private val areaCodeMap = mapOf(
        "11" to "北京",
        "12" to "天津",
        "13" to "河北",
        "14" to "山西",
        "15" to "内蒙古",
        "21" to "辽宁",
        "22" to "吉林",
        "23" to "黑龙江",
        "31" to "上海",
        "32" to "江苏",
        "33" to "浙江",
        "34" to "安徽",
        "35" to "福建",
        "36" to "江西",
        "37" to "山东",
        "41" to "河南",
        "42" to "湖北",
        "43" to "湖南",
        "44" to "广东",
        "45" to "广西",
        "46" to "海南",
        "50" to "重庆",
        "51" to "四川",
        "52" to "贵州",
        "53" to "云南",
        "54" to "西藏",
        "61" to "陕西",
        "62" to "甘肃",
        "63" to "青海",
        "64" to "宁夏",
        "65" to "新疆",
        "71" to "台湾",
        "81" to "香港",
        "82" to "澳门",
        "91" to "国外"
    )

    fun validateIDCard(idNum: String): ValidateResult {

        //=============== 号码长度  15位或者18位 =============
        if (idNum.length != 15 && idNum.length != 18) {
            return FAIL("身份证长度不正确")
        }
        //===============  end ============================

        //============= 除18位的身份证外，其余的都是数字
        var ai = ""
        if (idNum.length == 18) {
            ai = idNum.substring(0, 17)
        } else if (idNum.length == 15) {
            ai = "${idNum.substring(0, 6)}19${idNum.substring(6)}"
        }
        if (!isNumber(ai)) {
            return FAIL("请输入正确的身份证号")
        }
        //==================== end ======================

        //==============  判断出生年月是否有效
        val strYear = ai.substring(6, 10) //年
        val strMonth = ai.substring(10, 12)  //月
        val strDay = ai.substring(12, 14)    //日

        if (!isDate("${strYear}-${strMonth}-${strDay}")) {
            return FAIL("输入的身份证日期不正确")
        }

        if ((gc.get(Calendar.YEAR) - strYear.toInt()) > 150 || (gc.time.time - (sdf.parse("${strYear}-${strMonth}-${strDay}")?.time
                ?: 0)) < 0
        ) {
            return FAIL("请输入正确的身份证格式")
        }

        if (strMonth.toInt() > 12 || strMonth.toInt() <= 0) {
            return FAIL("身份证日期输入错误")
        }

        if (strDay.toInt() > 31 || strDay.toInt() <= 0) {
            return FAIL("身份证日期输入错误")
        }
        //==================== end =====================


        //=============== 判断地区码 ====================
        if (areaCodeMap[ai.substring(0, 2)] == null) {
            return FAIL("身份证地区编码错误")
        }
        //=============  end =============================

        //================= 判断最后一位的值 ====================
        //对身份证前17位进行加权累加
        val sdNumTotal = ai.mapIndexed { index, c -> (c.code - '0'.code) * wi[index] }.reduce { acc, i -> acc + i }
        val modeValue = sdNumTotal % 11
        val verifyCode = codeArr[modeValue]
        ai += verifyCode

        if (ai.length != 18) {
            return FAIL("身份证校验失败")
        }

        if (idNum != ai) {
            return FAIL("身份证校验位失败")
        }
        return SUCCESS
    }

    /**
     * 判断字符串是否都是数字
     */
    private fun isNumber(num: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNumber = pattern.matcher(num)
        return isNumber.matches()
    }

    /**
     * 判断日期的有效性
     */
    private fun isDate(strDate: String?): Boolean {
        val pattern =
            Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")
        val m = pattern.matcher(strDate)
        return m.matches()
    }


    sealed class ValidateResult()
    object SUCCESS : ValidateResult()
    data class FAIL(var reason: String) : ValidateResult()
}