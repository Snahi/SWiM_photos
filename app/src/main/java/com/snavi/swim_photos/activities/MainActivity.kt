package com.snavi.swim_photos.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.snavi.swim_photos.R
import com.snavi.swim_photos.display_images.ImagesAdapter
import com.snavi.swim_photos.display_images.SwipeToDeleteHandler
import com.snavi.swim_photos.images.Image
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.photo_card.*
import java.util.*

class MainActivity : AppCompatActivity() {



    companion object {
        const val REQ_CODE_ADD_PHOTO = 1
    }


    val m_images: ArrayList<Image> = ArrayList()

    lateinit var m_recyclerView     : RecyclerView
    lateinit var m_layoutManager    : RecyclerView.LayoutManager
    lateinit var m_adapter          : ImagesAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_layoutManager = LinearLayoutManager(this)
        m_adapter       = ImagesAdapter(m_images)

        m_recyclerView  = rl_photos.apply {
            setHasFixedSize(false)
            layoutManager = m_layoutManager
            adapter = m_adapter
        }

        // remove
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteHandler {a_holder ->
            val holderPosition = a_holder.adapterPosition
            m_images.removeAt(holderPosition)
            m_adapter.notifyItemRemoved(holderPosition)
            hideEmptyTextMessageIfNecessary()
        })

        itemTouchHelper.attachToRecyclerView(m_recyclerView)

        hideEmptyTextMessageIfNecessary()
    }



    override fun onCreateOptionsMenu(a_menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.action_bar, a_menu)

        return true
    }



    override fun onOptionsItemSelected(a_item: MenuItem): Boolean = when (a_item.itemId) {
        R.id.but_main_activity_add_new_photo -> openAddActivity()
        else -> super.onOptionsItemSelected(a_item)
    }



    override fun onActivityResult(a_reqCode: Int, a_resCode: Int, a_data: Intent?): Unit
    {
        if (a_resCode != Activity.RESULT_OK) return

        when (a_reqCode) {
            REQ_CODE_ADD_PHOTO -> addImage(a_data!!.getSerializableExtra(ActivityAddImage.INTENT_IMAGE_KEY) as Image)
            else -> throw Exception("Implementation error, return from activity that wasn't supposed to be called")
        }
    }




    private fun openAddActivity(): Boolean
    {
        val intent = Intent(this, ActivityAddImage::class.java)
        startActivityForResult(intent, REQ_CODE_ADD_PHOTO)

        return true
    }



    private fun addImage(a_image: Image)
    {
        m_images.add(0, a_image)
        m_adapter.notifyItemInserted(0)
        m_recyclerView.smoothScrollToPosition(0)
        hideEmptyTextMessageIfNecessary()
    }



    private fun hideEmptyTextMessageIfNecessary()
    {
        if (m_images.isEmpty()) tv_main_activity_no_photos_text.visibility = View.VISIBLE
        else tv_main_activity_no_photos_text.visibility = View.GONE
    }
}
