package com.fizzer.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fizzer.widget.R
import com.fizzer.widget.databinding.WidgetRecyclerViewIndicatorLayoutBinding

class RecyclerViewIndicator@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: WidgetRecyclerViewIndicatorLayoutBinding

    private var indicatorW = 0
    private var indicatorH = 0
    private var rate = 0f

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_recycler_view_indicator_layout, this)
        mBinding = WidgetRecyclerViewIndicatorLayoutBinding.bind(this)

    }

    fun bindWithRecyclerView(recyclerView: RecyclerView, orientation: Int) {
        val isHorizontal = orientation == RecyclerView.HORIZONTAL
        mBinding.rootView.post {
            val scrollRange : Float=
                if (isHorizontal) (recyclerView.computeHorizontalScrollRange()).toFloat() else (recyclerView.computeVerticalScrollRange()).toFloat()
            val scrollExtent =
                if (isHorizontal) (recyclerView.computeHorizontalScrollExtent()).toFloat() else (recyclerView.computeVerticalScrollExtent()).toFloat()

            val indicatorLp = mBinding.indicatorView.layoutParams
            if (isHorizontal) {
                rate = (width /scrollRange)
                indicatorW = (scrollExtent * rate).toInt()
                indicatorLp.height = LayoutParams.MATCH_PARENT
                indicatorLp.width = indicatorW
            } else {
                rate = (height / scrollRange)
                indicatorH = (scrollExtent * rate).toInt()
                indicatorLp.width = LayoutParams.MATCH_PARENT
                indicatorLp.height = indicatorH
            }
            mBinding.indicatorView.layoutParams = indicatorLp
        }
        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollOffset =
                    if (isHorizontal) recyclerView.computeHorizontalScrollOffset() else recyclerView.computeVerticalScrollOffset()
                if (isHorizontal) {
                    val left = (scrollOffset * rate).toInt()
                    mBinding.indicatorView.layout(
                        left,
                        mBinding.indicatorView.top,
                        left + indicatorW,
                        mBinding.indicatorView.bottom
                    )
                } else {
                    val top = (scrollOffset * rate).toInt()
                    mBinding.indicatorView.layout(
                        mBinding.indicatorView.left,
                        top,
                        mBinding.indicatorView.right,
                        top + indicatorH
                    )
                }
            }
        })
    }

}