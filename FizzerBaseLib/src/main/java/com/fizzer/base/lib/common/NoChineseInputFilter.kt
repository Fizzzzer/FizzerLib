package com.fizzer.base.lib.common

import android.text.InputFilter
import android.text.Spanned

/**
 *
 * @Author：Fizzer
 * @Email:fizzer503@gmail.com
 * @CreateDate：2024/4/9
 * @Desc：中文过滤器，限制用户的中文输入
 * 版本：1.0
 * 修订历史：
 * 使用方法： editText.filters = arrayOf(NoChineseInputFilter())
 */
class NoChineseInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?, start: Int, end: Int,
        dest: Spanned?, dstart: Int, dend: Int
    ): CharSequence? {
        if (source == null){
            return null
        }

        for (index in start until end){
            if(Character.UnicodeBlock.of(source[index]) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS){
                return ""
            }
        }
        return null
    }
}