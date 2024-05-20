package com.example.travel_test

import MyPopupMenu
import PageButtonClickListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MainContract.View {

    var lang = "zh-tw"

    private lateinit var presenter: MainContract.Presenter
    private lateinit var exchangeLanguageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() //隱藏應用程式本身的ToolBar

        val page1Button = findViewById<ImageButton>(R.id.page1)
        page1Button.setImageResource(R.drawable.home)
        val newsDetail = findViewById<TextView>(R.id.home)
        newsDetail.setTextColor(Color.parseColor("#00939F"))

        presenter = MainPresenter(this)

        // 檢查網路連接
        if (!NetworkUtils.isNetworkConnected(this)) {
            NetworkUtils.showNetworkErrorDialog(this)
        }else {
            presenter.getAttractions(lang)
            presenter.getNews(lang)
        }

        exchangeLanguageButton = findViewById(R.id.exchange_language) // 初始化按鈕
        exchangeLanguageButton.setOnClickListener {
            showLanguageMenu()
        }

        setupPageButtonClickListener()
    }

    private fun setupPageButtonClickListener() {
        val pageButtonClickListener = PageButtonClickListener(this, this::class.java, lang)
        pageButtonClickListener.setupButtons(
            findViewById(R.id.page1),
            findViewById(R.id.page2),
            findViewById(R.id.page3),
            findViewById(R.id.page4)
        )
    }

    override fun showNewsError(message: String) {
        Log.e("API News Call", message)
    }

    override fun showAttractionsError(message: String) {
        Log.e("API Attractions Call", message)
    }


    private fun showLanguageMenu() {
        val langMap = mapOf(
            R.id.menu_item_zh_tw to "zh-tw",
            R.id.menu_item_zh_cn to "zh-cn",
            R.id.menu_item_en to "en",
            R.id.menu_item_ja to "ja",
            R.id.menu_item_ko to "ko",
            R.id.menu_item_es to "es",
//            R.id.menu_item_id to "id", //API不支援印尼文
            R.id.menu_item_th to "th",
            R.id.menu_item_vi to "vi"
        )

        val popupMenu = MyPopupMenu(this, findViewById(R.id.exchange_language))
        popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedLang = langMap[menuItem.itemId] ?: lang
            lang = selectedLang
            presenter.getAttractions(lang) // 更新 API 請求的語言
            presenter.getNews(lang)

            // 更新 Toolbar 的標題文字
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            val titleMap = mapOf(
                "zh-tw" to "臺北旅遊",
                "zh-cn" to "台北旅游",
                "en" to "Taipei travel",
                "ja" to "台北旅行",
                "ko" to "타이페이 여행",
                "es" to "Viaje a Taipéi",
                "th" to "เที่ยวไทเป",
                "vi" to "Du lịch Đài Bắc"
            )

            val newsMap = mapOf(
                "zh-tw" to "最新消息",
                "zh-cn" to "最新消息",
                "en" to "Latest News",
                "ja" to "最新ニュース",
                "ko" to "최신 뉴스",
                "es" to "Noticias recientes",
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

            val moreButtonMap = mapOf(
                "zh-tw" to "更多",
                "zh-cn" to "更多",
                "en" to "More",
                "ja" to "もっと",
                "ko" to "더",
                "es" to "Más",
                "th" to "มากกว่า",
                "vi" to "Hơn"
            )

            when (lang) {
                "zh-tw", "zh-cn", "en", "ja", "ko", "es", "th", "vi" -> {
                    toolbar.title = titleMap[lang]
                    findViewById<TextView>(R.id.news).text = newsMap[lang]
                    findViewById<TextView>(R.id.attractions_title).text = attractionsMap[lang]
                    findViewById<TextView>(R.id.moreButton).text = moreButtonMap[lang]
                    findViewById<TextView>(R.id.moreButton2).text = moreButtonMap[lang]
                }
            }
            ToolbarHelper.setToolbarTexts(lang, findViewById(R.id.home), findViewById(R.id.news_detail), findViewById(R.id.attractions), findViewById(R.id.favorite_detail))
            setupPageButtonClickListener()
            true
        }
        popupMenu.show()
    }


    override fun showNews(news: List<NewsItem>) {
        for (newsItem in news) {
//            Log.d("New", newsItem.title)
//            Log.d("New", newsItem.description)
//            Log.d("Language Selected", "Selected Language: $lang")
            val newsView = findViewById<RecyclerView>(R.id.newsView)
            val moreButton = findViewById<Button>(R.id.moreButton)
            newsView.adapter = NewAdapter(news, moreButton,lang)
        }
    }

    // 印出所抓到的API資訊
    override fun showAttractions(attractions: List<Attraction>) {
        for (attraction in attractions) {
//            Log.d("Attraction", attraction.name)
//            Log.d("Attraction", attraction.introduction)
//            Log.d("Language Selected", "Selected Language: $lang")
            val attractionsView = findViewById<RecyclerView>(R.id.attractionsView)
            val moreButton2 = findViewById<Button>(R.id.moreButton2)
            attractionsView.adapter = AttractionAdapter(attractions, moreButton2, lang)
        }
    }


    class NewAdapter(private val news: List<NewsItem>, private val moreButton: Button, private val lang: String) : RecyclerView.Adapter<NewAdapter.NewViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
            return NewViewHolder(view, moreButton, lang)
        }

        override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
            val new = news[position]
            holder.bind(new)
        }

        override fun getItemCount(): Int {
            return if (news.size > 3) 3 else news.size // 最多顯示3個
        }

        class NewViewHolder(itemView: View, moreButton: Button, private val lang: String) : RecyclerView.ViewHolder(itemView) {
            private lateinit var new: NewsItem

            init {
                itemView.setOnClickListener {
                    println("News 點擊了")
                    if (::new.isInitialized) {
                        println("初始化")
                        val intent = new.createNewsIntent(itemView.context)
                        itemView.context.startActivity(intent)
                    }
                }
                moreButton.setOnClickListener {
                    println("more 點擊了")
                    val intent = new.createNewsIntent_all(itemView.context)
                    itemView.context.startActivity(intent)
                }
            }

            fun NewsItem.createNewsIntent(context: Context): Intent {
                return Intent(context, NewsActivity::class.java).apply {
                    putExtra("webview_url", url)
                }
            }

            fun NewsItem.createNewsIntent_all(context: Context): Intent {
                return Intent(context, SecondActivity::class.java).apply {
                    putExtra("news_lang", lang)
                }
            }

            fun bind(new: NewsItem) {
                this.new = new

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(dateFormat.parse(new.posted))

                with(itemView) {
                    findViewById<TextView>(R.id.newsTitleTextView).text = new.title
                    findViewById<TextView>(R.id.newsDescriptionTextView).text = new.description
                    findViewById<TextView>(R.id.newsPostedTextView).text = formattedDate
                }
            }
        }
    }



    class AttractionAdapter(private val attractions: List<Attraction>,private val moreButton2: Button, private val lang: String) : RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attraction, parent, false)
            return AttractionViewHolder(view,moreButton2, lang)
        }

        override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
            val attraction = attractions[position]
            holder.bind(attraction)
        }

        override fun getItemCount(): Int {
            return if (attractions.size > 3) 3 else attractions.size // 最多顯示3個
        }

        class AttractionViewHolder(itemView: View,moreButton2: Button, private val lang: String) : RecyclerView.ViewHolder(itemView) {
            private val attractionImageView: ImageView = itemView.findViewById(R.id.attractionImageView)
            private val attractionTelTextView: TextView = itemView.findViewById(R.id.attractionTelTextView)
            private val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
            private lateinit var attraction: Attraction

            init {
                itemView.setOnClickListener {
                    println("Attractions 點擊了")
                    if (::attraction.isInitialized) {
                        println("初始化")
                        val intent = attraction.createAttractionIntent(itemView.context)
                        itemView.context.startActivity(intent)
                    }
                }
                favoriteButton.visibility = View.GONE

                moreButton2.setOnClickListener {
                    println("more2 點擊了")
                    val intent = attraction.createAttractionIntent_all(itemView.context)
                    itemView.context.startActivity(intent)
                }
            }

            fun Attraction.createAttractionIntent(context: Context): Intent {
                return Intent(context, AttractionsActivity::class.java).apply {
                    putExtra("attraction_name", name)
                    putExtra("attraction_introduction", introduction)
                    putExtra("attraction_address", address)
                    putExtra("attraction_tel", tel)
                    putExtra("attraction_fax", fax)
                    putExtra("attraction_email", email)
                    putExtra("attraction_official_site", official_site)
                    putExtra("attraction_facebook", facebook)
                    putExtra("attraction_image_url", images.firstOrNull()?.src)
                    val imageUrls_all = images.map { it.src }
                    putStringArrayListExtra("attraction_image_urls_all", ArrayList(imageUrls_all))
                }
            }

            fun Attraction.createAttractionIntent_all(context: Context): Intent {
                return Intent(context, ThirdActivity::class.java).apply {
                    putExtra("attractions_lang", lang)
                }
            }


            fun bind(attraction: Attraction) {
                this.attraction = attraction

                with(itemView) {
                    findViewById<TextView>(R.id.attractionNameTextView).text = attraction.name
                    findViewById<TextView>(R.id.attractionAddressTextView).text = attraction.address
                    findViewById<TextView>(R.id.attractionTelTextView).text = attraction.tel
                }

                if (attraction.images.isNotEmpty()) {
                    // 取得第一張圖片的 URL
                    val imageUrl = attraction.images[0].src
                    // 使用 Picasso 載入圖片
                    Picasso.get().load(imageUrl).into(attractionImageView)
                } else {
                    // 如果沒有圖片，則清除 ImageView 中的內容
                    attractionImageView.setImageDrawable(null)
                    attractionImageView.setImageResource(R.drawable.no_image)
                    // 設為不可見
//                    attractionImageView.visibility = View.GONE
                }

                    // 檢查是否有電話號碼
                if (attraction.tel.isEmpty()) {
                    // 如果電話號碼為空，則設為不可見
                    attractionTelTextView.visibility = View.GONE
                }
            }
        }
    }

}

