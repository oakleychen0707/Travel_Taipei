package com.example.travel_test

import PageButtonClickListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class SecondActivity : AppCompatActivity() {

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        supportActionBar?.hide() //隱藏應用程式本身的ToolBar

        val page2Button = findViewById<ImageButton>(R.id.page2)
        page2Button.setImageResource(R.drawable.news)
        val newsDetail = findViewById<TextView>(R.id.news_detail)
        newsDetail.setTextColor(Color.parseColor("#00939F"))

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val lang = intent.getStringExtra("news_lang") ?: "zh-tw"

        presenter = SecondPresenter(this)


        if (!NetworkUtils.isNetworkConnected(this)) {
            NetworkUtils.showNetworkErrorDialog(this)
        }else {
            presenter.getNews(lang)
        }

        // 更新 Toolbar 的標題文字
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val titleMap = mapOf(
            "zh-tw" to "最新消息",
            "zh-cn" to "最新消息",
            "en" to "Latest News",
            "ja" to "最新ニュース",
            "ko" to "최신 뉴스",
            "es" to "Noticias recientes",
            "th" to "ข่าวล่าสุด",
            "vi" to "tin mới nhất"
        )

        ToolbarHelper.setToolbarTexts(lang, findViewById(R.id.home), findViewById(R.id.news_detail), findViewById(R.id.attractions), findViewById(R.id.favorite_detail))

        when (lang) {
            "zh-tw", "zh-cn", "en", "ja", "ko", "es", "th", "vi" -> {
                toolbar.title = titleMap[lang]
            }
        }


        val pageButtonClickListener = PageButtonClickListener(this, supportFragmentManager, this::class.java, lang)
        pageButtonClickListener.setupButtons(
            findViewById(R.id.page1),
            findViewById(R.id.page2),
            findViewById(R.id.page3),
            findViewById(R.id.page4)
        )
    }

    fun showNewsError(message: String) {
        Log.e("API News Call", message)
    }

    fun showNews(news: List<NewsItem>) {
        for (newsItem in news) {
//            Log.d("New", newsItem.title)
//            Log.d("New", newsItem.description)
//            Log.d("Language Selected", "Selected Language: $lang")

            val newsView = findViewById<RecyclerView>(R.id.newView_all)
            newsView.adapter = NewAdapter_all(news)
        }
    }


    class NewAdapter_all(private val news: List<NewsItem>) : RecyclerView.Adapter<NewAdapter_all.NewViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_all, parent, false)
            return NewViewHolder(view)
        }

        override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
            val new = news[position]
            holder.bind(new)
        }

        override fun getItemCount(): Int {
            return news.size
        }

        class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            }

            fun NewsItem.createNewsIntent(context: Context): Intent {
                return Intent(context, NewsActivity::class.java).apply {
                    putExtra("webview_url", url)
                }
            }

            fun bind(new: NewsItem) {
                this.new = new

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(dateFormat.parse(new.posted))
                val formattedDate_modified = dateFormat.format(dateFormat.parse(new.modified))

                with(itemView) {
                    findViewById<TextView>(R.id.newsTitleTextView).text = new.title
                    findViewById<TextView>(R.id.newsDescriptionTextView).text = new.description
                    findViewById<TextView>(R.id.newsPostedTextView).text = formattedDate
                    findViewById<TextView>(R.id.newsModifiedTextView).text = formattedDate_modified
                }
            }
        }
    }
}