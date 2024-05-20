package com.example.travel_test

import PageButtonClickListener
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        supportActionBar?.hide() // 隱藏應用程式本身的ToolBar

        setupPage3Button()
        setupBackButton()
        setupLanguage()
        setupToolbarText()

        val fragment = createAttractionsFragment()
        addFragment(fragment)
    }

    private fun setupPage3Button() {
        val page3Button = findViewById<ImageButton>(R.id.page3)
        page3Button.setImageResource(R.drawable.attractions)
        val attractionsDetail = findViewById<TextView>(R.id.attractions)
        attractionsDetail.setTextColor(Color.parseColor("#00939F"))
    }

    private fun setupBackButton() {
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupLanguage() {
        val lang = intent.getStringExtra("lang") ?: "zh-tw" // 使用預設值 "zh-tw" 避免空指標異常
        val pageButtonClickListener = PageButtonClickListener(this, this::class.java, lang)
        pageButtonClickListener.setupButtons(
            findViewById(R.id.page1),
            findViewById(R.id.page2),
            findViewById(R.id.page3),
            findViewById(R.id.page4)
        )
    }

    private fun setupToolbarText() {
        val lang = intent.getStringExtra("lang") ?: "zh-tw"
        val titleMap = mapOf(
            "zh-tw" to "旅遊景點",
            "zh-cn" to "旅游景点",
            "en" to "Tourist Attractions",
            "ja" to "観光名所",
            "ko" to "관광 명소",
            "es" to "Atracciones Turísticas",
            "th" to "สถานที่ท่องเที่ยว",
            "vi" to "điểm thu hút khách du lịch"
        )

        val toolbar = findViewById<TextView>(R.id.toolbar_title)
        toolbar.text = titleMap[lang]
        ToolbarHelper.setToolbarTexts(lang, findViewById(R.id.home), findViewById(R.id.news_detail), findViewById(R.id.attractions), findViewById(R.id.favorite_detail))
    }

    private fun createAttractionsFragment(): Fragment {
        val lang = intent.getStringExtra("lang") ?: "zh-tw"
        val fragment = FragmentAttractions_1()
        val bundle = Bundle().apply {
            putString("lang", lang)
        }
        fragment.arguments = bundle
        return fragment
    }

    private fun addFragment(fragment: Fragment) {
        val frameLayout = findViewById<FrameLayout>(R.id.fragment_attractions_1)
        supportFragmentManager.beginTransaction().apply {
            replace(frameLayout.id, fragment)
            commit()
        }
    }
}
