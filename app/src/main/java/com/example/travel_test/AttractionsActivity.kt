package com.example.travel_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso

class AttractionsActivity : AppCompatActivity() {

    private var currentPosition = 0
    private lateinit var viewPager: ViewPager
    private lateinit var arrowLeft: ImageButton
    private lateinit var arrowRight: ImageButton
    private lateinit var attractionImageUrlsAll: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attractions)
        supportActionBar?.hide()

        initView()

        bindDataToViews()

        setListeners()

        handleImagePagerVisibility()
    }

    private fun initView() {
        viewPager = findViewById(R.id.viewPager)
        arrowLeft = findViewById(R.id.arrowLeft)
        arrowRight = findViewById(R.id.arrowRight)
    }

    private fun bindDataToViews() {
        val viewsMap = mapOf(
            "attraction_name" to R.id.attractionNameTextView,
            "attraction_introduction" to R.id.attractionIntroductionTextView,
            "attraction_address" to R.id.attractionAddressTextView,
            "attraction_tel" to R.id.attractionTelTextView,
            "attraction_fax" to R.id.attractionFaxTextView,
            "attraction_email" to R.id.attractionEmailTextView,
            "attraction_official_site" to R.id.attractionOfficial_siteTextView,
            "attraction_facebook" to R.id.attractionFacebookTextView
        )

        for ((attractionKey, viewId) in viewsMap) {
            val attrValue = intent.getStringExtra(attractionKey)
            findViewById<TextView>(viewId).applyVisibility(attrValue)
        }

        val officialSiteUrl = intent.getStringExtra("attraction_official_site")
        findViewById<TextView>(R.id.attractionOfficial_siteTextView).text = officialSiteUrl
    }

    private fun setListeners() {
        arrowLeft.setOnClickListener {
            navigateImagePager(-1)
        }

        arrowRight.setOnClickListener {
            navigateImagePager(1)
        }

        val attractionOfficialSiteTextView = findViewById<View>(R.id.attractionOfficial_siteTextView) as? TextView
        attractionOfficialSiteTextView?.setOnClickListener {
            openWebView(attractionOfficialSiteTextView.text.toString())
        }
    }

    private fun handleImagePagerVisibility() {
        attractionImageUrlsAll = intent.getStringArrayListExtra("attraction_image_urls_all") ?: ArrayList()
        if (attractionImageUrlsAll.isNullOrEmpty()) {
            viewPager.visibility = View.GONE
            arrowLeft.visibility = View.GONE
            arrowRight.visibility = View.GONE
        } else {
            viewPager.adapter = ImagePagerAdapter(attractionImageUrlsAll)
        }
    }

    private fun navigateImagePager(direction: Int) {
        val newPosition = currentPosition + direction
        if (newPosition >= 0 && newPosition < attractionImageUrlsAll.size) {
            currentPosition = newPosition
            viewPager.setCurrentItem(currentPosition, true)
            arrowLeft.visibility = if (currentPosition == 0) View.GONE else View.VISIBLE
            arrowRight.visibility = if (currentPosition == attractionImageUrlsAll.size - 1) View.GONE else View.VISIBLE
        }
    }

    private fun TextView.applyVisibility(text: String?) {
        if (text.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            this.text = text
        }
    }

    private fun openWebView(url: String) {
        val intent = Intent(this, NewsActivity::class.java)
        intent.putExtra("webview_url", url)
        startActivity(intent)
    }
}





