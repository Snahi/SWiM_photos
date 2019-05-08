package com.snavi.swim_photos.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.snavi.swim_photos.R
import com.snavi.swim_photos.images.Image
import com.snavi.swim_photos.images.URL_REGEX
import kotlinx.android.synthetic.main.activity_add_image.*
import java.util.*
import kotlin.collections.ArrayList


const val TAG_REGEX = """^[a-zA-Z0-9]*${'$'}"""    // letters and numbers only


class ActivityAddImage : AppCompatActivity() {



    companion object {
        const val DATE_ELEMENTS_SEPARATOR  = "/"
        const val TAGS_SEPARATOR           =","
        const val INTENT_IMAGE_KEY         = "i"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)

        editText_add_image_date.inputType = 0   // in xml wasn't working. 0 is none. User can only choose from picker

        editText_add_image_title.onFocusChangeListener = View.OnFocusChangeListener {_, a_hasFocus -> checkForErrorsInTitle(a_hasFocus)}
        editText_add_image_url.onFocusChangeListener   = View.OnFocusChangeListener {_, a_hasFocus -> checkForErrorsInUrl(a_hasFocus)}

        // for date two listeners, because if onFocus only then it is necessary to lose focus to chose date again.
        // if only onClick, then it is necessary to click twice
        editText_add_image_date.onFocusChangeListener  = View.OnFocusChangeListener {_, a_hasFocus -> pickDate(a_hasFocus)}
        editText_add_image_date.setOnClickListener { pickDate(true) }

        editText_add_image_tags.onFocusChangeListener = View.OnFocusChangeListener {_, a_hasFocus -> checkForErrorsInTags(a_hasFocus)}

        add_image_but_add.setOnClickListener { onAddButtonClicked() }
    }



    // title ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun checkForErrorsInTitle(a_hasFocus: Boolean)
    {
        if (!a_hasFocus)
        {
            if (!isTitleValid())
                editText_add_image_title.error = getString(R.string.add_image_title_error)
            else
                editText_add_image_title.error = null
        }
    }



    private fun isTitleValid(): Boolean
    {
        return editText_add_image_title.text.isNotBlank()
    }



    // url /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun checkForErrorsInUrl(a_hasFocus: Boolean)
    {
        if (!a_hasFocus)
        {
            if (!isUrlValid())
                editText_add_image_url.error = getString(R.string.add_image_url_error)
            else
                editText_add_image_url.error = null

        }
    }



    private fun isUrlValid(): Boolean
    {
        return URL_REGEX.toRegex().matches(editText_add_image_url.text)
    }



    // date ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * user can choose date from date picker, but it's not obligatory. If not chosen, then current date will be set
     */
    private fun pickDate(a_hasFocus: Boolean)
    {
        if (a_hasFocus)
        {
            val calendar     = Calendar.getInstance()
            val currentDay   = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear  = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Dialog,
                DatePickerDialog.OnDateSetListener
                    { _, year, monthOfYear, dayOfMonth ->
                        editText_add_image_date.setText(
                            String.format("%d$DATE_ELEMENTS_SEPARATOR%d$DATE_ELEMENTS_SEPARATOR%d", dayOfMonth, monthOfYear, year))
                    },
                currentYear,
                currentMonth,
                currentDay
            )

            datePickerDialog.show()
        }
    }



    // tags ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun checkForErrorsInTags(a_hasFocus: Boolean)
    {
        if (!a_hasFocus)
        {
            if (!areTagsValid())
                editText_add_image_tags.error = getString(R.string.add_image_tags_error)
            else
                editText_add_image_tags.error = null
        }
    }



    private fun areTagsValid(): Boolean
    {
        val tags = editText_add_image_tags.text
        val split = tags.split(TAGS_SEPARATOR)

        val tagRegex = TAG_REGEX.toRegex()

        for (tag in split)
        {
            if (!tagRegex.matches(tag))
            {
                return false
            }
        }

        return true
    }



    // add button //////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun onAddButtonClicked()
    {
        if (areAllFieldsCorrect())
        {
            val title  = editText_add_image_title.text.toString()
            val url     = editText_add_image_url.text.toString()
            val date    = getDate()
            val tags    = getTags()

            val image = if (rb_add_image.isChecked)
                Image(title, url, date, tags, true)
            else
                Image(title, url, date, tags, false)

            val intent = Intent()
            intent.putExtra(INTENT_IMAGE_KEY, image)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        else
            showAllErrors()

    }



    // errors //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun areAllFieldsCorrect(): Boolean = isTitleValid() && isUrlValid() && areTagsValid()


    /**
     * function shows errors wherever necessary
     */
    private fun showAllErrors()
    {
        checkForErrorsInTitle(false)
        checkForErrorsInUrl(false)
        checkForErrorsInTags(false)
    }



    // process inputs //////////////////////////////////////////////////////////////////////////////////////////////////
    private fun getDate(): Calendar
    {
        if (editText_add_image_date.text.isNotBlank())
        {
            val date = editText_add_image_date.text.split(DATE_ELEMENTS_SEPARATOR)

            val calendar = Calendar.getInstance()
            calendar.set(date[2].toInt(), date[1].toInt(), date[0].toInt())

            return calendar
        }

        return Calendar.getInstance()
    }



    private fun getTags(): ArrayList<String>
    {
        val split = editText_add_image_tags.text.split(TAGS_SEPARATOR)

        val noBlank = split.filter {elem -> elem.isNotBlank()}

        return ArrayList(noBlank)
    }

}
