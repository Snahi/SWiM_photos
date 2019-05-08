package com.snavi.swim_photos.activities

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.snavi.swim_photos.R
import com.snavi.swim_photos.display_images.ImagesAdapter
import com.snavi.swim_photos.fragments.FullScreenImageFragment
import com.snavi.swim_photos.fragments.PhotoDataFragment
import com.snavi.swim_photos.fragments.SimilarPhotosFragment
import com.snavi.swim_photos.images.Image
import com.snavi.swim_photos.touch_listeners.OnSwipeListener
import kotlinx.android.synthetic.main.activity_lookup.*
import kotlin.math.min


/**
 * class containing fragments with full screen view of chosen photo, fragment with photo data (title, date), fragment
 * with tags
 */
class LookupActivity : FragmentActivity() {


    companion object
    {
        const val MAX_NUM_OF_SIMILAR_PHOTOS = 6

        const val IMAGES_KEY        = "IMAGES"
        const val MAIN_IMG_IDX_KEY  = "MAIN_IMG"
    }



    private lateinit var m_images  : ArrayList<Image>
    private lateinit var m_mainImg : Image
    private var m_mainImgIdx        = -1



    override fun onCreate(a_savedInstanceState: Bundle?)
    {
        super.onCreate(a_savedInstanceState)
        setContentView(R.layout.activity_lookup)

        // fields
        m_images     = intent.getSerializableExtra(IMAGES_KEY) as ArrayList<Image>
        m_mainImgIdx = intent.getIntExtra(MAIN_IMG_IDX_KEY, -1)
        m_mainImg    = m_images[m_mainImgIdx]

        // create fragments
        val fullScreenPhotoFragment = createFullScreenPhotoFragment()
        val photoDataFragment       = createPhotoDataFragment()
        val similarPhotosFragment   = createSimilarPhotosFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fl_activity_lookup_full_screen_holder,     fullScreenPhotoFragment)
        fragmentTransaction.add(R.id.fl_activity_lookup_photo_data_holder,      photoDataFragment)
        fragmentTransaction.add(R.id.fl_activity_lookup_similar_photos_holder,  similarPhotosFragment)

        fragmentTransaction.commit()

        // so that when user swipes on text "similar photos", it also returns. Other swipeListeners are in fragments
        setBackSwipeOnSimilarLabel()
    }



    private fun createFullScreenPhotoFragment(): FullScreenImageFragment
    {
        val bundleWithPhoto = Bundle()
        bundleWithPhoto.putString(FullScreenImageFragment.IMAGE_KEY, m_mainImg.m_url)
        val fullScreenPhotoFragment = FullScreenImageFragment()
        fullScreenPhotoFragment.arguments = bundleWithPhoto

        return fullScreenPhotoFragment
    }



    private fun createPhotoDataFragment(): PhotoDataFragment
    {
        val bundleWithPhotoData = Bundle()
        bundleWithPhotoData.putString(PhotoDataFragment.TITLE_KEY, m_mainImg.m_title)
        bundleWithPhotoData.putString(PhotoDataFragment.URL_KEY, m_mainImg.m_url)
        bundleWithPhotoData.putString(PhotoDataFragment.DATE_KEY, android.text.format.DateFormat.format(ImagesAdapter.DATE_FORMAT, m_mainImg.m_date).toString())
        bundleWithPhotoData.putSerializable(PhotoDataFragment.TAGS_KEY, m_mainImg.m_tags)
        val photoDataFragment = PhotoDataFragment()
        photoDataFragment.arguments = bundleWithPhotoData

        return photoDataFragment
    }



    private fun createSimilarPhotosFragment(): SimilarPhotosFragment
    {
        val bundleWithSimilarPhotos = Bundle()
        bundleWithSimilarPhotos.putSerializable(SimilarPhotosFragment.SIMILAR_PHOTOS_KEY, findSimilarImages())
        val similarPhotosFragment = SimilarPhotosFragment()
        similarPhotosFragment.arguments = bundleWithSimilarPhotos

        return similarPhotosFragment
    }



    private fun setBackSwipeOnSimilarLabel()
    {
        tv_activity_lookup_similar_label.setOnTouchListener(
            OnSwipeListener(this,
                onSwipeRight =
                {
                    fl_activity_lookup_full_screen_holder.visibility    = View.VISIBLE
                    fl_activity_lookup_photo_data_holder.visibility     = View.GONE
                    fl_activity_lookup_similar_photos_holder.visibility = View.GONE
                    tv_activity_lookup_similar_label.visibility         = View.GONE
                })
        )
    }



    private fun findSimilarImages(): ArrayList<Image>
    {
        val imagesWithSimilarity = ArrayList<Pair<Int, Image>>(MAX_NUM_OF_SIMILAR_PHOTOS)   // first in pair - size of intersection with main image

        for (img in m_images)
            imagesWithSimilarity.add(Pair(intersectSize(img), img))

        imagesWithSimilarity.removeAt(m_mainImgIdx)         // remove itself
        imagesWithSimilarity.sortByDescending {it.first}    // sort by num of equal tags

        // take most similar ones
        val res = ArrayList<Image>(MAX_NUM_OF_SIMILAR_PHOTOS)
        for (i in 0..min(MAX_NUM_OF_SIMILAR_PHOTOS - 1, imagesWithSimilarity.size - 1))
            if (imagesWithSimilarity[i].first > 0) res.add(imagesWithSimilarity[i].second)      // must have at least one common tag

        return res
    }



    private fun intersectSize(a_img: Image): Int = a_img.m_tags.intersect(m_mainImg.m_tags).size

}
