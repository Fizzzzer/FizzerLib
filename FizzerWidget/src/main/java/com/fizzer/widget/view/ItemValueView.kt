package com.fizzer.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.fizzer.widget.R
import com.fizzer.widget.databinding.WidgetItemValueLayoutBinding

class ItemValueView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: WidgetItemValueLayoutBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_item_value_layout, this)
        binding = WidgetItemValueLayoutBinding.bind(this)
        initAttr(attrs)
    }

    private fun initAttr(attr: AttributeSet?) {
        attr?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.ItemValueView)

            val itemName = ta.getString(R.styleable.ItemValueView_iv_item_name)

            val itemValue = ta.getString(R.styleable.ItemValueView_iv_item_value)
            val showRightArrow = ta.getBoolean(R.styleable.ItemValueView_iv_item_show_arrow, true)
            val valueDrawable = ta.getDrawable(R.styleable.ItemValueView_iv_item_icon)

            binding.itemName.text = itemName
            binding.itemValue.text = itemValue
            binding.rightArrow.visibility = if (showRightArrow) VISIBLE else GONE
            valueDrawable?.let { drawable ->
                binding.itemIcon.visibility = VISIBLE
                binding.itemIcon.setImageDrawable(drawable)
            } ?: let {
                binding.itemIcon.visibility = GONE
            }
        }
    }

    /**
     * valueText的文案
     */
    var valueText: String? = ""
        get() = binding.itemValue.text.toString()
        set(value) {
            field = value
            binding.itemValue.text = value ?: ""
        }

    /**
     * valueText的文字颜色
     */
    var valueTextColor: Int = -1
        set(value) {
            field = value
            binding.itemValue.setTextColor(value)
        }

    /**
     * itemName的文案
     */
    var itemName: String? = ""
        get() = binding.itemName.text.toString()
        set(value) {
            field = value
            binding.itemName.text = value ?: ""
        }

    /**
     * 设置箭头是否可见
     */
    fun setArrowVisible(visible: Int) {
        binding.rightArrow.visibility = visible
    }
}