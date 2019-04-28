package com.snavi.swim_photos.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snavi.swim_photos.R
import com.snavi.swim_photos.activities.LookupActivity
import com.snavi.swim_photos.touch_listeners.OnSwipeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_lookup.*
import kotlinx.android.synthetic.main.full_screen_image_fragment.view.*


class FullScreenImageFragment : Fragment() {


    companion object
    {
        const val IMAGE_KEY = "IMG"
    }


    override fun onCreateView(a_inflater: LayoutInflater, a_container: ViewGroup?, a_savedInstanceState: Bundle?): View?
    {
        val layout = a_inflater.inflate(R.layout.full_screen_image_fragment, a_container, false)
        val imageUrl = arguments?.getString(IMAGE_KEY)
        Picasso.get().load(imageUrl).into(layout.img_full_screen_image)

        layout.setOnTouchListener(OnSwipeListener(context!!,
            onSwipeLeft =
            {
                (activity as LookupActivity).fl_activity_lookup_full_screen_holder.visibility    = View.GONE
                (activity as LookupActivity).fl_activity_lookup_photo_data_holder.visibility     = View.VISIBLE
                (activity as LookupActivity).fl_activity_lookup_similar_photos_holder.visibility = View.VISIBLE
                (activity as LookupActivity).tv_activity_lookup_similar_label.visibility         = View.VISIBLE
            }))

        return layout
    }
}