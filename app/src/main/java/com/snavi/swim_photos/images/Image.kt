package com.snavi.swim_photos.images

import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.Serializable
import java.util.*


const val URL_REGEX = """[-a-zA-Z0-9@:%_\+.~#?&//=]{2,256}\.[a-z]{2,4}\b(\/[-a-zA-Z0-9@:%_\+.~#?&//=]*)"""
const val IMAGE_FAIL_TAG = "imgFail"


class Image (var m_title:    String,
             var m_url:      String,
             var m_date:     Calendar,
             var m_tags:     ArrayList<String>,
             var m_autotags: Boolean) : Serializable {


    /**
     * @return false - if this url would cause exception, true - otherwise
     */
    fun into(a_imageView: ImageView): Boolean
    {
        if (m_url.isBlank()) return false

        Picasso.get().load(m_url).into(a_imageView)

        return true
    }

}