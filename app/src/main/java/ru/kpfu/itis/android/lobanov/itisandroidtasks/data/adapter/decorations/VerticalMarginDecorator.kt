package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalMarginDecorator(
    private val itemOffset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        with(outRect) {
            top = itemOffset
            bottom = itemOffset
        }
    }
}