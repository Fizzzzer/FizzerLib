package com.fizzer.base.lib.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/7
 * @Descriptor: 时间戳转换工具类
 */
object TimeFormatUtils {

    /**
     * 获取当前的时间戳
     */
    fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }


    /**
     * 获取格式化的时间
     */
    fun getFormatTime(format :SimpleDateFormat,time:Long):String{
        return format.format(Date(time))
    }



    /**
     *将秒转化成 小时：分钟：秒
     */
    fun formatMiss(miss: Int): String {
        val hh = if (miss / 3600 > 9) (miss / 3600).toString() else "0" + miss / 3600
        val mm =
            if (miss % 3600 / 60 > 9) (miss % 3600 / 60).toString() else "0" + miss % 3600 / 60
        val ss =
            if (miss % 3600 % 60 > 9) (miss % 3600 % 60).toString() else "0" + miss % 3600 % 60
        return "$hh:$mm:$ss"
    }

    /**
     * 将秒转化成 分：秒
     */
    fun formatMiss2(m: Int): String {
        val miss = abs(m)
        val mm =
            if (miss % 3600 / 60 > 9) (miss % 3600 / 60).toString() else "0" + miss % 3600 / 60
        val ss =
            if (miss % 3600 % 60 > 9) (miss % 3600 % 60).toString() else "0" + miss % 3600 % 60
        return "$mm:$ss"
    }

    /**
     * 获取两个日期相差多少天
     */
    fun getDateBy2Day(startStr: String, endStr: String, format: SimpleDateFormat): Long {
        val startDate = format.parse(startStr)
        val endDate = format.parse(endStr)
        return (endDate.time - startDate.time) / (60 * 60 * 24 * 1000)
    }

    /**
     * 计算目标时间距离现在的时间间隔
     */
    fun diffTime(diff: Long): String {
        val d = "0:0:0:0"
        val sb = StringBuilder()

        try {
            val days = diff / (1000 * 60 * 60 * 24)
            val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val minutes =
                (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
            val second =
                (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000
            if (days > 0L) {
                sb.append("${days}天 ")
            }
            if (hours > 0L) {
                sb.append("${hours}:")
            }
            if (minutes > 0L) {
                sb.append("${minutes}:")
            }
            sb.append(second)

            return sb.toString()
        } catch (e: Exception) {
        }
        return d
    }

    /**
     * 判断是否为同一天
     */
    fun isSameDay(time1: Long, time2: Long): Boolean {
        return isSameDay(Date(time1), Date(time2))
    }

    /**
     * 判断是否为同一天
     */
    fun isSameDay(date1: Date, date2: Date): Boolean {
        return (getYear(date1) == getYear(date2)
                && getMonth(date1) == getMonth(date2)
                && getDay(date1) == getDay(date2))
    }

    /**
     * 获取年份
     */
    fun getYear(date: Date): Int {
        val calendar = getCalendar(date)
        return calendar.get(Calendar.YEAR)
    }

    /**
     * 获取月份
     */
    fun getMonth(date: Date): Int {
        val calendar = getCalendar(date)
        // Calendar的月份是从0开始的
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 获取日
     */
    fun getDay(date: Date): Int {
        val calendar = getCalendar(date)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 获取小时
     */
    fun getHour(date: Date): Int {
        return getCalendar(date).get(Calendar.HOUR) + 1
    }

    /**
     * 获取分钟
     */
    fun getMinute(date: Date): Int {
        return getCalendar(date).get(Calendar.MINUTE)
    }

    /**
     * 获取日历对象
     */
    @JvmStatic
    fun getCalendar(date: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    /**
     * 获取周数，既当前是一年中的第几周
     */
    @JvmStatic
    fun getWeekOfYear(date: Date): Int {
        val calendar = getCalendar(date)
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }





    @SuppressLint("ConstantLocale")
    object Format {
        val formatYMD1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatYMD2 = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val formatYMD3 = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val formatYMDHMS1 = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val formatYMDHMS2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val formatYMDHM1 = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        val formatYMDHM2 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val formatMDHM1 = SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault())
        val formatMDHM2 = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
        val formatMDHM3 = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())

        val formatM = SimpleDateFormat("MM", Locale.getDefault())
        val formatY = SimpleDateFormat("yyyy", Locale.getDefault())

        val formatYM1 = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val formatYM2 = SimpleDateFormat("yyyy/MM", Locale.getDefault())
        val formatYM3 = SimpleDateFormat("yyyy年MM月", Locale.getDefault())

        val formatMD1 = SimpleDateFormat("M月d日", Locale.getDefault())
        val formatMD2 = SimpleDateFormat("MM月dd日", Locale.getDefault())
        val formatMD3 = SimpleDateFormat("MM/dd", Locale.getDefault())
        val formatMD4 = SimpleDateFormat("MM-dd", Locale.getDefault())

        val formatHM1 = SimpleDateFormat("HH:mm", Locale.getDefault())

        val formatHMS1 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formatHMS2 = SimpleDateFormat("HH时mm分ss秒", Locale.getDefault())

        val formatD = SimpleDateFormat("dd", Locale.getDefault())

        val formatHMYMD = SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault())


    }
}