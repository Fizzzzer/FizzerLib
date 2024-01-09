package com.fizzer.base.lib.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/13
 * @Description: App跳转工具类
 * 主要是跳转到APP的一些常用内置应用
 */
object AppJumpUtils {
    private object Market{
        //小米应用商店
        const val PACKAGE_MI_MARKET = "com.xiaomi.market"
        const val MI_MARKET_PAGE = "com.xiaomi.market.ui.AppDetailActivity"

        //魅族应用商店
        const val PACKAGE_MEIZU_MARKET = "com.meizu.mstore"
        const val MEIZU_MARKET_PAGE = "com.meizu.flyme.appcenter.activitys.AppMainActivity"

        //VIVO应用商店
        const val PACKAGE_VIVO_MARKET = "com.bbk.appstore"
        const val VIVO_MARKET_PAGE = "com.bbk.appstore.ui.AppStoreTabActivity"

        //OPPO应用商店
        const val PACKAGE_OPPO_MARKET = "com.oppo.market"
        const val OPPO_MARKET_PAGE = "a.a.a.aoz"

        //华为应用商店
        const val PACKAGE_HUAWEI_MARKET = "com.huawei.appmarket"
        const val HUAWEI_MARKET_PAGE = "com.huawei.appmarket.service.externalapi.view.ThirdApiActivity"

        //ZTE应用商店
        const val PACKAGE_ZTE_MARKET = "zte.com.market"
        const val ZTE_MARKET_PAGE = "zte.com.market.view.zte.drain.ZtDrainTrafficActivity"

        //360手机助手
        const val PACKAGE_360_MARKET = "com.qihoo.appstore"
        const val PACKAGE_360_PAGE = "com.qihoo.appstore.distribute.SearchDistributionActivity"

        //酷市场 -- 酷安网
        const val PACKAGE_COOL_MARKET = "com.coolapk.market"
        const val COOL_MARKET_PAGE = "com.coolapk.market.activity.AppViewActivity"

        //应用宝
        const val PACKAGE_TENCENT_MARKET = "com.tencent.android.qqdownloader"
        const val TENCENT_MARKET_PAGE = "com.tencent.pangu.link.LinkProxyActivity"

        //PP助手
        const val PACKAGE_ALI_MARKET = "com.pp.assistant"
        const val ALI_MARKET_PAGE = "com.pp.assistant.activity.MainActivity"

        //豌豆荚
        const val PACKAGE_WANDOUJIA_MARKET = "com.wandoujia.phoenix2"

        // 低版本可能是 com.wandoujia.jupiter.activity.DetailActivity
        const val WANDOUJIA_MARKET_PAGE = "com.pp.assistant.activity.PPMainActivity"

        //UCWEB
        const val PACKAGE_UCWEB_MARKET = "com.UCMobile"
        const val UCWEB_MARKET_PAGE = "com.pp.assistant.activity.PPMainActivity"
    }

    /**
     * 调用系统拨打电话
     */
    @JvmStatic
    fun callPhone(context: Context?, phone: String?) {
        if (phone.isNullOrEmpty()) {
            return
        }
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phone")
        intent.data = data
        context?.startActivity(intent)
    }

    /***
     * 打开应用市场
     * @param mContext
     * @param marketPackageName
     */
    fun openGoogleMarket(mContext: Context, marketPackageName: String?) {
        try {
            //谷歌商店
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://play.google.com/store/apps/details?id=$marketPackageName")
            mContext.startActivity(i)
        } catch (anf: ActivityNotFoundException) {
            Log.e("MarketUtils", "要跳转的应用市场不存在!")
        } catch (e: Exception) {
            Log.e("MarketUtils", "其他错误：" + e.message)
        }
    }

    fun gotoMarket(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //        intent.setData(Uri.parse("market://details?id=" + "com.jrd.beauty"));
        intent.data = Uri.parse("market://details?id=" + AppInfoUtils.getPkgName(context))
        val keys = getKeys(context)
        if (keys != null) {
            intent.setClassName(keys[0]!!, keys[1]!!)
        }
        //修复某些老手机会因为找不到任何市场而报错
        if (AppInfoUtils.isAppAvailable(context, intent.`package`)) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "无可用应用市场", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 获取应用商店配置
     */
    private fun getKeys(context: Context): Array<String?>? {
        val keys = arrayOfNulls<String>(2)
        if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_MI_MARKET)) {
            keys[0] = Market.PACKAGE_MI_MARKET
            keys[1] = Market.MI_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_VIVO_MARKET)) {
            keys[0] = Market.PACKAGE_VIVO_MARKET
            keys[1] = Market.VIVO_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_OPPO_MARKET)) {
            keys[0] = Market.PACKAGE_OPPO_MARKET
            keys[1] = Market.OPPO_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_HUAWEI_MARKET)) {
            keys[0] = Market.PACKAGE_HUAWEI_MARKET
            keys[1] = Market.HUAWEI_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_ZTE_MARKET)) {
            keys[0] = Market.PACKAGE_ZTE_MARKET
            keys[1] = Market.ZTE_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_COOL_MARKET)) {
            keys[0] = Market.PACKAGE_COOL_MARKET
            keys[1] = Market.COOL_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_360_MARKET)) {
            keys[0] = Market.PACKAGE_360_MARKET
            keys[1] = Market.PACKAGE_360_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_MEIZU_MARKET)) {
            keys[0] = Market.PACKAGE_MEIZU_MARKET
            keys[1] = Market.MEIZU_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_TENCENT_MARKET)) {
            keys[0] = Market.PACKAGE_TENCENT_MARKET
            keys[1] = Market.TENCENT_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_ALI_MARKET)) {
            keys[0] = Market.PACKAGE_ALI_MARKET
            keys[1] = Market.ALI_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_WANDOUJIA_MARKET)) {
            keys[0] = Market.PACKAGE_WANDOUJIA_MARKET
            keys[1] = Market.WANDOUJIA_MARKET_PAGE
        } else if (AppInfoUtils.isAppAvailable(context, Market.PACKAGE_UCWEB_MARKET)) {
            keys[0] = Market.PACKAGE_UCWEB_MARKET
            keys[1] = Market.UCWEB_MARKET_PAGE
        }
        return if (TextUtils.isEmpty(keys[0])) {
            null
        } else {
            keys
        }
    }
}