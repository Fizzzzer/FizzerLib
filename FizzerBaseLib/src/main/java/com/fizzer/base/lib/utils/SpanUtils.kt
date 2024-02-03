package com.fizzer.base.lib.utils

import android.content.Context
import android.graphics.MaskFilter
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import android.widget.TextView
import java.util.*

/**
 *
 * @Author：Fizzer
 * @Email:fizzer503@gmail.com
 * @CreateDate：2024/1/11
 * @Desc：Spanner工具类
 *  版本：1.0
 *  修订历史：
 */
object SpanUtils {

    fun create(): SpanBuilder {
        return SpanBuilder()
    }

    class SpanBuilder {
        /**
         * 获得当前 SpanStringBuilder 实例
         */
        private var spanStrBuilder: SpannableStringBuilder?

        init {
            spanStrBuilder = SpannableStringBuilder("")
        }

        /**
         * 添加文字片段
         */
        fun addSection(section: CharSequence?): SpanBuilder {
            spanStrBuilder!!.append(section)
            return this
        }

        /**
         * 添加字体片段
         *
         * @param section 要添加的文字
         * @param span    任何span
         * @param flag    可以是
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 不包含 section 前后文字，最常用
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE 包含 section 前面文字
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE 包含 section 后面文字
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE 包含 section 前后文字
         * ……
         * @return SpanBuilder
         */
        fun addCertainSection(section: String?, span: Any?, flag: Int): SpanBuilder {
            val start = spanStrBuilder!!.length
            spanStrBuilder!!.append(section)
            val end = spanStrBuilder!!.length
            spanStrBuilder!!.setSpan(span, start, end, flag)
            return this
        }

        /**
         * 添加带前景色的文字片段
         */
        fun addForeColorSection(section: String?, color: Int): SpanBuilder {
            return addCertainSection(
                section,
                ForegroundColorSpan(color),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        /**
         * 添加带背景色的文字片段
         */
        fun addBackColorSection(section: String?, color: Int): SpanBuilder {
            return addCertainSection(
                section,
                BackgroundColorSpan(color),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        /**
         * 添加下标
         */
        fun addSubSection(section: String?): SpanBuilder {
            return addCertainSection(section, SubscriptSpan(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        /**
         * 添加上标
         */
        fun addSuperSection(section: String?): SpanBuilder {
            return addCertainSection(section, SuperscriptSpan(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        /**
         * 添加下划线片段
         */
        fun addUnderlineSection(section: String?): SpanBuilder {
            return addCertainSection(section, UnderlineSpan(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        /**
         * 添加删除线片段
         */
        fun addStrickoutSection(section: String?): SpanBuilder {
            return addCertainSection(section, StrikethroughSpan(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        /**
         * 添加绝对大小字体片段
         */
        fun addAbsSizeSection(section: String?, size: Int): SpanBuilder {
            return addCertainSection(
                section,
                AbsoluteSizeSpan(size, true),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        /**
         * 添加相对大小字体片段
         */
        fun addRelSizeSection(section: String?, proportion: Float): SpanBuilder {
            return addCertainSection(
                section,
                RelativeSizeSpan(proportion),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        /**
         * 添加 url 字体片段
         */
        fun addURLSection(section: String?, url: String?): SpanBuilder {
            return addCertainSection(section, URLSpan(url), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        /**
         * 添加某种风格（Style）的文字片段
         *
         * @param section
         * @param style   TypeFace.BOLD  加粗
         * TypeFace.BOLD_ITALIC  加粗倾斜
         * TypeFace.ITALIC  倾斜
         * TypeFace.NORMAL  正常
         * @return
         */
        fun addStyleSection(section: String?, style: Int): SpanBuilder {
            return addCertainSection(section, StyleSpan(style), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        /**
         * 添加某种字体的文字片段
         *
         * @param section
         * @param family  The font family for this typeface.  Examples include
         * "monospace", "serif", and "sans-serif".
         */
        fun addTypefaceSection(section: String?, family: String?): SpanBuilder {
            return addCertainSection(
                section,
                TypefaceSpan(family),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        /**
         * 添加带 Mask 的文字片段
         *
         * @param maskFilter BlurMaskFilter、EmbossMaskFilter
         * 例：//Blur a character
         * new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
         * //Emboss a character
         * new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
         * @return
         */
        fun addMaskSection(section: String?, maskFilter: MaskFilter?): SpanBuilder {
            return addCertainSection(
                section,
                MaskFilterSpan(maskFilter),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        private fun onDecor(
            section: String,
            ignoreCase: Boolean,
            which: Which,
            decorCallback: DecorCallback
        ): SpanBuilder {
            var section = section
            var baseStr = string
            if (ignoreCase) {
                section = section.uppercase(Locale.getDefault())
                baseStr = baseStr.uppercase(Locale.getDefault())
            }
            var start = 0
            when (which) {
                Which.FIRST -> {
                    start = baseStr.indexOf(section)
                    if (start != -1) decorCallback.decor(start, start + section.length)
                }
                Which.LAST -> {
                    start = baseStr.lastIndexOf(section)
                    if (start != -1) decorCallback.decor(start, start + section.length)
                }
                Which.ALL -> while (true) {
                    start = baseStr.indexOf(section, start)
                    if (start == -1) break
                    decorCallback.decor(start, start + section.length)
                    start += section.length
                }
            }
            return this
        }

        private interface DecorCallback {
            fun decor(start: Int, end: Int)
        }

        /**
         * 加上前景色
         *
         * @param ignoreCase boolean,true 区分大小写；false, 不区分大小写
         */
        fun setForeColor(
            section: String,
            color: Int,
            ignoreCase: Boolean,
            which: Which
        ): SpanBuilder {
            return onDecor(section, ignoreCase, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setForeColor(color, start, end)
                }
            })
        }

        /**
         * 给最后该片段（section）加上前景色，不区分大小写
         */
        fun setForeColor(section: String, color: Int): SpanBuilder {
            return setForeColor(section, color, true, Which.LAST)
        }

        /**
         * 整体加上前景色
         */
        fun setForeColor(color: Int): SpanBuilder {
            return setForeColor(color, 0, spanStrBuilder!!.length)
        }

        /**
         * 加上前景色
         */
        fun setForeColor(color: Int, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 加上背景色
         */
        fun setBackColor(
            section: String,
            color: Int,
            ignoreCase: Boolean,
            which: Which
        ): SpanBuilder {
            return onDecor(section, ignoreCase, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setBackColor(color, start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）加上背景色，不区分大小写
         */
        fun setBackColor(section: String, color: Int): SpanBuilder {
            return setBackColor(section, color, true, Which.LAST)
        }

        /**
         * 整体加上背景色
         */
        fun setBackColor(color: Int): SpanBuilder {
            return setBackColor(color, 0, spanStrBuilder!!.length)
        }

        /**
         * 加上背景色
         */
        fun setBackColor(color: Int, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                BackgroundColorSpan(color),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 设置为下标
         */
        fun setAsSubscript(section: String, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setAsSubscript(start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）设置为下标
         */
        fun setAsSubscript(section: String): SpanBuilder {
            return setAsSubscript(section, Which.LAST)
        }

        /**
         * 整体设置为下标
         */
        fun setAsSubscript(): SpanBuilder {
            return setAsSubscript(0, spanStrBuilder!!.length)
        }

        /**
         * 设置为下标
         */
        fun setAsSubscript(start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return this
        }

        /**
         * 设置为上标
         */
        fun setAsSuperscript(section: String, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setAsSuperscript(start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）设置为上标
         */
        fun setAsSuperscript(section: String): SpanBuilder {
            return setAsSuperscript(section, Which.LAST)
        }

        /**
         * 整体设置为上标
         */
        fun setAsSuperscript(): SpanBuilder {
            return setAsSuperscript(0, spanStrBuilder!!.length)
        }

        /**
         * 设置为上标
         */
        fun setAsSuperscript(start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                SuperscriptSpan(),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 给片段设置下划线
         */
        fun setUnderline(section: String, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setUnderline(start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）设置下划线
         */
        fun setUnderline(section: String): SpanBuilder {
            return setUnderline(section, Which.LAST)
        }

        /**
         * 给所有文字设置下划线
         */
        fun setUnderline(): SpanBuilder {
            return setUnderline(0, spanStrBuilder!!.length)
        }

        /**
         * 给片段设置下划线
         */
        fun setUnderline(start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return this
        }

        /**
         * 给片段添加删除线
         */
        fun setStrikethrough(section: String, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setStrikethrough(start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）添加删除线
         */
        fun setStrikethrough(section: String): SpanBuilder {
            return setStrikethrough(section, Which.LAST)
        }

        /**
         * 给所有文字添加删除线
         */
        fun setStrikethrough(): SpanBuilder {
            return setStrikethrough(0, spanStrBuilder!!.length)
        }

        /**
         * 给片段添加删除线
         */
        fun setStrikethrough(start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                StrikethroughSpan(),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 给片段的字体设置绝对大小
         */
        fun setAbsSize(section: String, size: Int, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setAbsSize(size, start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）设置绝对大小
         */
        fun setAbsSize(section: String, size: Int): SpanBuilder {
            return setAbsSize(section, size, Which.LAST)
        }

        /**
         * 给所有文字设置绝对大小
         */
        fun setAbsSize(size: Int): SpanBuilder {
            return setAbsSize(size, 0, spanStrBuilder!!.length)
        }

        /**
         * 给片段的字体设置绝对大小
         */
        fun setAbsSize(size: Int, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                AbsoluteSizeSpan(size, true),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 给片段的字体设置相对大小
         */
        fun setRelSize(section: String, proportion: Float, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setRelSize(proportion, start, end)
                }
            })
        }

        /**
         * 给最后一个该片段的字体设置相对大小
         */
        fun setRelSize(section: String, proportion: Float): SpanBuilder {
            return setRelSize(section, proportion, Which.LAST)
        }

        /**
         * 给片段的字体设置相对大小
         */
        fun setRelSize(proportion: Float): SpanBuilder {
            return setRelSize(proportion, 0, spanStrBuilder!!.length)
        }

        /**
         * 给片段的字体设置相对大小
         */
        fun setRelSize(proportion: Float, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                RelativeSizeSpan(proportion),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 转为 url 字体片段
         */
        fun setAsURL(section: String, url: String?, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setAsUrl(url, start, end)
                }
            })
        }

        /**
         * 整体转为 url 字体片段
         */
        fun setAsUrl(url: String?): SpanBuilder {
            return setAsUrl(url, 0, spanStrBuilder!!.length)
        }

        /**
         * 转为 url 字体片段
         */
        fun setAsUrl(url: String?, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(URLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return this
        }

        /**
         * 设置字体风格（粗、斜、正常）
         */
        fun setStyle(section: String, style: Int, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setStyle(style, start, end)
                }
            })
        }

        /**
         * 给最后一个片段（section）设置字体风格（粗、斜、正常）
         */
        fun setStyle(section: String, style: Int): SpanBuilder {
            return setStyle(section, style, Which.LAST)
        }

        /**
         * 整体设置字体风格（粗、斜、正常）
         */
        fun setStyle(style: Int): SpanBuilder {
            return setStyle(style, 0, spanStrBuilder!!.length)
        }

        /**
         * 设置字体风格（粗、斜、正常）
         */
        fun setStyle(style: Int, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return this
        }

        /**
         * 设置字体
         *
         * @param family "monospace", "serif", and "sans-serif"
         * @param which  SpanUtil.Which.FIRST、SpanUtil.Which.LAST、SpanUtil.Which.ALL
         */
        fun setTypeface(section: String, family: String?, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setTypeface(family, start, end)
                }
            })
        }

        /**
         * 给最后一个该片段（section）设置字体
         */
        fun setTypeface(section: String, family: String?): SpanBuilder {
            return setTypeface(section, family, Which.LAST)
        }

        /**
         * 整体设置字体
         */
        fun setTypeface(family: String?): SpanBuilder {
            return setTypeface(family, 0, spanStrBuilder!!.length)
        }

        /**
         * 设置字体
         */
        fun setTypeface(family: String?, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                TypefaceSpan(family),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 设置 Mask
         *
         * @param maskFilter BlurMaskFilter、EmbossMaskFilter
         * 例：//Blur((使…) 变模糊) a character
         * new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
         * //Emboss a character
         * new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
         */
        fun setMask(section: String, maskFilter: MaskFilter?, which: Which): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    setMask(maskFilter, start, end)
                }
            })
        }

        /**
         * 为最后一个该片段（section）设置 Mask
         *
         * @param maskFilter BlurMaskFilter、EmbossMaskFilter
         * 例：//Blur a character
         * new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
         * //Emboss a character
         * new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
         */
        fun setMask(section: String, maskFilter: MaskFilter?): SpanBuilder {
            return setMask(section, maskFilter, Which.LAST)
        }

        /**
         * 为整体设置 Mask
         *
         * @param maskFilter BlurMaskFilter、EmbossMaskFilter
         * 例：//Blur a character
         * new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
         * //Emboss a character
         * new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
         */
        fun setMask(maskFilter: MaskFilter?): SpanBuilder {
            return setMask(maskFilter, 0, spanStrBuilder!!.length)
        }

        /**
         * 设置 Mask
         *
         * @param maskFilter BlurMaskFilter、EmbossMaskFilter
         * 例：//Blur a character
         * new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
         * //Emboss a character
         * new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
         * @param start      开始位置
         * @param end        截止位置
         */
        fun setMask(maskFilter: MaskFilter?, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                MaskFilterSpan(maskFilter),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 设置对齐方式
         *
         * @param alignment Layout.Alignment.ALIGN_CENTER 居中
         * Layout.Alignment.ALIGN_NORMAL 正常（左对齐）
         * Layout.Alignment.ALIGN_OPPOSITE 反向（右对齐）
         * @param start     开始位置
         * @param end       截止位置
         */
        fun setAlign(alignment: Layout.Alignment?, start: Int, end: Int): SpanBuilder {
            spanStrBuilder!!.setSpan(
                AlignmentSpan.Standard(alignment!!),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 文字后添加图片
         *
         * @param resId
         * @return
         */
        fun addImage(context: Context?, resId: Int): SpanBuilder {
            insertImage(context, resId, spanStrBuilder!!.length)
            return this
        }

        /**
         * 文字中某位置（where）插入图片
         *
         * @param resId 图片资源 Id
         * @param where 插入位置：占一个字的位置，整体索引增加一个
         * @return
         */
        fun insertImage(context: Context?, resId: Int, where: Int): SpanBuilder {
            spanStrBuilder!!.insert(where, " ")
            spanStrBuilder!!.setSpan(
                ImageSpan(context!!, resId),
                where,
                where + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return this
        }

        /**
         * 清除格式
         */
        fun clearSpans(): SpanBuilder {
            spanStrBuilder!!.clearSpans()
            return this
        }
        /**
         * 移除格式
         */
        /**
         * 移除最后一个该片段（section）的格式
         */
        @JvmOverloads
        fun removeSpans(section: String, which: Which = Which.LAST): SpanBuilder {
            return onDecor(section, false, which, object : DecorCallback {
                override fun decor(start: Int, end: Int) {
                    removeSpans(start, end)
                }
            })
        }

        /**
         * 移除格式，从某一个下标开始
         */
        fun removeSpans(section: String, fromIndex: Int): SpanBuilder {
            var fromIndex = fromIndex
            val baseStr = string
            fromIndex = baseStr.indexOf(section, fromIndex)
            removeSpans(fromIndex, fromIndex + section.length)
            return this
        }

        /**
         * 移除格式
         *
         * @param start 开始位置
         * @param end   结束位置
         * @return this
         */
        fun removeSpans(start: Int, end: Int): SpanBuilder {
            val spans = spanStrBuilder!!.getSpans(start, end, Any::class.java)
            for (span in spans) {
                spanStrBuilder!!.removeSpan(span)
            }
            return this
        }

        /**
         * 获得当前 SpanStringBuilder 中的字符串
         */
        val string: String
            get() = spanStrBuilder.toString()

        /**
         * 显示到控件
         *
         * @param textView
         */
        fun showIn(textView: TextView) {
            textView.text = spanStrBuilder
            spanStrBuilder!!.clearSpans()
            spanStrBuilder!!.clear()
            spanStrBuilder = null
        }
    }

    /**
     * 标记第一个、最后一个、所有
     */
    enum class Which {
        FIRST, LAST, ALL
    }
}