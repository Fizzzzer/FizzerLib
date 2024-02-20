package com.fizzer.lib.widget.RecyclerView

import com.fizzer.widget.recycler.TagItemDecoration

data class ItemData(var title: String) : TagItemDecoration.IItemTitle {
    override fun getTitleTag() = title.substring(0, 1)
}
