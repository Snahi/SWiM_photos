package com.snavi.swim_photos.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snavi.swim_photos.R
import com.snavi.swim_photos.activities.LookupActivity
import com.snavi.swim_photos.display_images.SimilarPhotosAdapter
import com.snavi.swim_photos.images.Image
import com.snavi.swim_photos.touch_listeners.OnSwipeListener
import kotlinx.android.synthetic.main.activity_lookup.*
import kotlinx.android.synthetic.main.fragment_similar_photos.view.*

class SimilarPhotosFragment : Fragment() {


    companion object
    {
        const val NUM_OF_COLS = 3
        const val SIMILAR_PHOTOS_KEY = "SIM"
    }


    private lateinit var m_recyclerView     : RecyclerView
    private lateinit var m_layoutManager    : RecyclerView.LayoutManager
    private lateinit var m_adapter          : SimilarPhotosAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_similar_photos, container, false)

        m_layoutManager = GridLayoutManager(context, NUM_OF_COLS)
        m_adapter = SimilarPhotosAdapter(arguments?.get(SIMILAR_PHOTOS_KEY) as ArrayList<Image>, context!!)
        m_recyclerView = layout.rv_similar_photos.apply {
            setHasFixedSize(true)
            layoutManager   = m_layoutManager
            adapter         = m_adapter
        }


        m_recyclerView.setOnTouchListener(OnSwipeListener(context!!,
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
