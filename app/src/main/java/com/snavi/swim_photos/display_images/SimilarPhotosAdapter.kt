package com.snavi.swim_photos.display_images

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.ImageView
import com.snavi.swim_photos.images.Image

class SimilarPhotosAdapter(val m_similarImages: ArrayList<Image>, val m_context: Context) : RecyclerView.Adapter<SimilarPhotosAdapter.PhotoHolder>() {



    class PhotoHolder(val m_imgView: ImageView) : RecyclerView.ViewHolder(m_imgView)



    override fun onCreateViewHolder(a_parent: ViewGroup, a_viewType: Int): PhotoHolder
    {
        val imageView = ImageView(a_parent.context)

        val displayMetrics = DisplayMetrics()
        (m_context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val imgDim = displayMetrics.widthPixels / MAX_NUMBER_OF_TAGS_TO_DISPLAY + 1

        imageView.layoutParams = RecyclerView.LayoutParams(imgDim, imgDim)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return PhotoHolder(imageView)
    }



    override fun getItemCount(): Int = m_similarImages.size



    override fun onBindViewHolder(a_holder: PhotoHolder, a_itemIdx: Int)
    {
        m_similarImages[a_itemIdx].into(a_holder.m_imgView)
    }
}