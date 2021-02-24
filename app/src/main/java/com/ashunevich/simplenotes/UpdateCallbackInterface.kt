package com.ashunevich.simplenotes

import androidx.recyclerview.widget.RecyclerView

interface UpdateCallbackInterface {
    fun onViewBound(viewHolder: RecyclerView.ViewHolder?, position: Int)
}