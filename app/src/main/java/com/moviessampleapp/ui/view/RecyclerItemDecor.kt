package com.moviessampleapp.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Nadeem on 20/10/2021.
 */
class RecyclerItemDecor(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = space
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
    }
}