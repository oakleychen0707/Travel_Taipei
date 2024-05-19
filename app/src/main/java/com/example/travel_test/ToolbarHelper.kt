package com.example.travel_test

import android.widget.TextView


object ToolbarHelper {
    fun setToolbarTexts(lang: String, homeTextView: TextView, newsDetailTextView: TextView, attractionsTextView: TextView, favoriteTextView: TextView) {

        val homeMap = mapOf(
            "zh-tw" to "首頁",
            "zh-cn" to "首页",
            "en" to "Front page",
            "ja" to "表紙",
            "ko" to "첫 장",
            "es" to "Página delantera",
            "th" to "หน้าแรก",
            "vi" to "Trang đầu"
        )

        val newsDetailMap = mapOf(
            "zh-tw" to "最新消息",
            "zh-cn" to "最新消息",
            "en" to "Latest news",
            "ja" to "最新メッセージ",
            "ko" to "최근 뉴스",
            "es" to "Últimas noticias",
            "th" to "ข่าวล่าสุด",
            "vi" to "tin mới nhất"
        )

        val attractionsMap = mapOf(
            "zh-tw" to "旅遊景點",
            "zh-cn" to "旅游景点",
            "en" to "Tourist Attractions",
            "ja" to "観光名所",
            "ko" to "관광 명소",
            "es" to "Atracciones Turísticas",
            "th" to "สถานที่ท่องเที่ยว",
            "vi" to "điểm thu hút khách du lịch"
        )

        val favoriteMap = mapOf(
            "zh-tw" to "收藏項目",
            "zh-cn" to "收藏项目",
            "en" to "Collection Items",
            "ja" to "コレクションアイテム",
            "ko" to "컬렉션 아이템",
            "es" to "Artículos de colección",
            "th" to "รายการคอลเลกชัน",
            "vi" to "Vật phẩm sưu tập"
        )

        homeTextView.text = homeMap[lang]
        newsDetailTextView.text = newsDetailMap[lang]
        attractionsTextView.text = attractionsMap[lang]
        favoriteTextView.text = favoriteMap[lang]
    }
}
