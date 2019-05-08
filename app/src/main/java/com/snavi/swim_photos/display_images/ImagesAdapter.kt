package com.snavi.swim_photos.display_images

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.snavi.swim_photos.R
import com.snavi.swim_photos.activities.LookupActivity
import com.snavi.swim_photos.images.IMAGE_FAIL_TAG
import com.snavi.swim_photos.images.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_card.view.*
import java.lang.Exception
import java.lang.Math.min


const val MAX_NUMBER_OF_TAGS_TO_DISPLAY = 3


class ImagesAdapter(val m_images: ArrayList<Image>, val m_mainContext: Context) : RecyclerView.Adapter<ImagesAdapter.PhotoHolder>() {


    companion object {
        const val DATE_FORMAT = "dd-MM-yyyy"
        const val TAGS_SEPARATOR = ","



        fun createTagsString(a_tags: Collection<String>): String
        {
            val res = StringBuilder()

            for (tag in a_tags) res.append("$tag$TAGS_SEPARATOR ")

            if (res.isNotEmpty()) return res.dropLast(2).toString() // drop comma

            return res.toString()
        }



        fun getTagsFromString(a_tagsString: String): ArrayList<String>
        {
            val tagsList: List<String> = a_tagsString.split(ImagesAdapter.TAGS_SEPARATOR)

            // clean from blank spaces
            val cleanTags = ArrayList<String>(tagsList.size)

            for (tag in tagsList) cleanTags.add(tag.trim())

            return cleanTags
        }
    }



    inner class PhotoHolder(val m_card: CardView) : RecyclerView.ViewHolder(m_card)
    {
        val img     : ImageView = m_card.img_photo_card_photo
        val title   : TextView  = m_card.tv_photo_card_title_of_photo
        val date    : TextView  = m_card.tv_photo_card_date
        val tags    : TextView  = m_card.tv_photo_card_tags
    }



    override fun onCreateViewHolder(a_parent: ViewGroup, a_viewType: Int): PhotoHolder
    {
        val card = LayoutInflater
            .from(a_parent.context)
            .inflate(R.layout.photo_card, a_parent, false) as CardView

        return PhotoHolder(card)
    }



    override fun getItemCount() = m_images.size



    override fun onBindViewHolder(a_holder: PhotoHolder, a_itemIdx: Int)
    {
        val image = m_images[a_itemIdx]

        image.into(a_holder.img)
        a_holder.title.text = image.m_title

        val dateFormatted = android.text.format.DateFormat.format(DATE_FORMAT, image.m_date)
        a_holder.date.text = dateFormatted

        if (image.m_autotags) createAutoTags(image.m_url, a_holder.m_card, image.m_tags)
        else a_holder.tags.text = createTagsString(image.m_tags)

        setHolderOnClickListener(a_holder)
    }



    private fun setHolderOnClickListener(a_holder: PhotoHolder)
    {
        a_holder.m_card.setOnClickListener {
            val intent = Intent(m_mainContext, LookupActivity::class.java)
            intent.putExtra(LookupActivity.MAIN_IMG_IDX_KEY, a_holder.adapterPosition)

            intent.putExtra(LookupActivity.IMAGES_KEY, m_images)
            m_mainContext.startActivity(intent)
        }
    }



    // auto-tags ////////////////////////////////////////////////////////////////////////////////////////////////////////

    lateinit var target: com.squareup.picasso.Target // only to prevent target from being gcollected

    private fun createAutoTags(a_image: String, a_card: CardView, a_oldTags: ArrayList<String>)
    {
        target = object: com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onBitmapLoaded(a_image: Bitmap?, from: Picasso.LoadedFrom?) {
                if (a_image == null) return
                tagWithFirebase(a_image, a_oldTags, a_card)
            }
        }

        Picasso.get().load(a_image).into(target)
    }



    private fun tagWithFirebase(a_image: Bitmap, a_oldTags: ArrayList<String>, a_card: CardView)
    {
        val visionImage: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(a_image)
        val labeler: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler

        labeler.processImage(visionImage)
            .addOnSuccessListener()
            {a_tags ->
                addToTags(a_tags, a_oldTags, a_card.tv_photo_card_tags)
            }
            .addOnFailureListener()
            {exc ->
                Log.e(IMAGE_FAIL_TAG, exc.message)
            }
    }



    private fun addToTags(a_firebaseTags: List<FirebaseVisionImageLabel>,
                          a_oldTags: ArrayList<String>,
                          a_tagsView: TextView)
    {
        val lastTagIdx = min(a_firebaseTags.size - 1, MAX_NUMBER_OF_TAGS_TO_DISPLAY - 1)

        for (tagIdx in 0..lastTagIdx) a_oldTags.add(a_firebaseTags[tagIdx].text)

        a_tagsView.text = createTagsString(a_oldTags.take(MAX_NUMBER_OF_TAGS_TO_DISPLAY))
    }
}