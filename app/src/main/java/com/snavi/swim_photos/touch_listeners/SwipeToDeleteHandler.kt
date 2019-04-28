package com.snavi.swim_photos.touch_listeners

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.snavi.swim_photos.display_images.ImagesAdapter


class SwipeToDeleteHandler(private val onDelete: (ImagesAdapter.PhotoHolder) -> Unit)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
            = false

    override fun onSwiped(a_viewHolder: RecyclerView.ViewHolder, direction: Int) {
        a_viewHolder.let {
            val photoHolder = a_viewHolder as ImagesAdapter.PhotoHolder
            onDelete(photoHolder)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {

        if (viewHolder.adapterPosition < 0) return

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}