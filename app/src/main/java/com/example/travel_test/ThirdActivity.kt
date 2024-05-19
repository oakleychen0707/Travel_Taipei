package com.example.travel_test

import PageButtonClickListener
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        supportActionBar?.hide() //隱藏應用程式本身的ToolBar

        val page3Button = findViewById<ImageButton>(R.id.page3)
        page3Button.setImageResource(R.drawable.attractions)
        val attractionsDetail = findViewById<TextView>(R.id.attractions)
        attractionsDetail.setTextColor(Color.parseColor("#00939F"))

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val pageButtonClickListener = PageButtonClickListener(this, supportFragmentManager, this::class.java)
        pageButtonClickListener.setupButtons(
            findViewById(R.id.page1),
            findViewById(R.id.page2),
            findViewById(R.id.page3),
            findViewById(R.id.page4),
        )

        val lang = intent.getStringExtra("attractions_lang") ?: "zh-tw" // 使用預設值 "zh-tw" 避免空指標異常

        // 更新 Toolbar 的標題文字
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
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

        ToolbarHelper.setToolbarTexts(lang, findViewById(R.id.home), findViewById(R.id.news_detail), findViewById(R.id.attractions))

        when (lang) {
            "zh-tw", "zh-cn", "en", "ja", "ko", "es", "th", "vi" -> {
                toolbar.title = titleMap[lang]
            }
        }

        // 創建 Fragment 實例
        val fragment = FragmentAttractions_1()

        // 將 lang 值放入 Bundle 中
        val bundle = Bundle()
        bundle.putString("attractions_lang", lang)
        fragment.arguments = bundle

        // FrameLayout
        val frameLayout = findViewById<FrameLayout>(R.id.fragment_attractions_1)

        // 將 Fragment 加入 Activity 中
        supportFragmentManager.beginTransaction().apply {
            replace(frameLayout.id, fragment)
            commit()
        }
        // FrameLayout
    }
}