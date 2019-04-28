package com.snavi.swim_photos.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snavi.swim_photos.R
import com.snavi.swim_photos.activities.LookupActivity
import com.snavi.swim_photos.display_images.ImagesAdapter
import com.snavi.swim_photos.touch_listeners.OnSwipeListener
import kotlinx.android.synthetic.main.activity_lookup.*
import kotlinx.android.synthetic.main.fragment_photo_data.view.*


class PhotoDataFragment : Fragment() {


    companion object
    {
        const val TITLE_KEY = "TIT"
        const val DATE_KEY  = "DATE"
        const val URL_KEY   = "URL"
        const val TAGS_KEY  = "TAG"
    }



    override fun onCreateView(
        a_inflater: LayoutInflater, a_container: ViewGroup?,
        a_savedInstanceState: Bundle?
    ): View?
    {
        val layout = a_inflater.inflate(R.layout.fragment_photo_data, a_container, false)
        layout.tv_fragment_photo_data_title.text = arguments?.getString(TITLE_KEY)
        layout.tv_fragment_photo_data_url.text = arguments?.getString(URL_KEY)
        layout.tv_fragment_photo_data_date.text = arguments?.getString(DATE_KEY)
        layout.tv_fragment_photo_data_tags.text = ImagesAdapter
            .createTagsString(arguments?.getSerializable(TAGS_KEY) as ArrayList<String>)

        layout.setOnTouchListener(
            OnSwipeListener(context!!,
                onSwipeRight =
                {
                    (activity as LookupActivity).fl_activity_lookup_full_screen_holder.visibility    = View.VISIBLE
                    (activity as LookupActivity).fl_activity_lookup_photo_data_holder.visibility     = View.GONE
                    (activity as LookupActivity).fl_activity_lookup_similar_photos_holder.visibility = View.GONE
                    (activity as LookupActivity).tv_activity_lookup_similar_label.visibility         = View.GONE
                })
        )

        return layout
    }



}
