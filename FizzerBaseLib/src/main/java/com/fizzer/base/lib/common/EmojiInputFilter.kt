package com.fizzer.base.lib.common

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class EmojiInputFilter : InputFilter {
    // 正则表达式，用于匹配大多数常见的 Emoji 表情
    private val emojiPattern: Pattern = Pattern.compile(
        "[\uD83C\uDF00-\uD83D\uDE4F]|" +  // 杂项符号和象形文字
                "[\uD83D\uDE80-\uD83D\uDEFF]|" +  // 交通和地图符号
                "[\u2600-\u27BF]|" +  // 杂项符号
                "[\uD83C\uDDE6-\uD83C\uDDFF]|" +  // 国旗
                "[\uD83E\uDD00-\uD83E\uDFFF]|" +  // 补充象形文字
                "[\u1F600-\u1F64F]|" +  // 表情符号
                "[\u1F300-\u1F5FF]|" +  // 各种符号和图标
                "[\u1F900-\u1F9FF]",  // 补充符号和象形文字
        Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
    )

    override fun filter(
        source: CharSequence?,
        start: Int, end: Int,
        dest: Spanned?, dstart: Int, dend: Int
    ): CharSequence? {

        if (source == null) {
            return null
        }

        // 检查输入的字符是否包含 Emoji 表情
        val matcher = emojiPattern.matcher(source)
        return if (matcher.find()) {
            ""  // 如果包含 Emoji 表情，返回空字符串，表示过滤掉这些字符
        } else {
            null  // 返回 null 表示不对输入做任何修改
        }
    }
}