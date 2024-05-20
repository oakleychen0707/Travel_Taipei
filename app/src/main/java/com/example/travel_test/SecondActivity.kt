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

        supportActionBar?.hide()

        initializeViews()
        setupPresenter()
        setupLanguageToolbar()
        setupPageButtons()
    }

    private fun initializeViews() {
        findViewById<ImageButton>(R.id.page2).apply {
            setImageResource(R.drawable.news)
        }
        findViewById<TextView>(R.id.news_detail).apply {
            setTextColor(Color.parseColor("#00939F"))
        }

        findViewById<ImageButton>(R.id.back_button).apply {
            setOnClickListener {
                finish()
            }
        }
    }

    private fun setupPresenter() {
        presenter = SecondPresenter(this)
        if (!NetworkUtils.isNetworkConnected(this)) {
            NetworkUtils.showNetworkErrorDialog(this)
        } else {
            presenter.getNews(intent.getStringExtra("news_lang") ?: "zh-tw")
        }
    }

    private fun setupLanguageToolbar() {
        val lang = intent.getStringExtra("news_lang") ?: "zh-tw"
        val toolbar = findViewById<TextView>(R.id.toolbar_title)
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
        toolbar.text = titleMap[lang]
        ToolbarHelper.setToolbarTexts(lang, findViewById(R.id.home), findViewById(R.id.news_detail), findViewById(R.id.attractions), findViewById(R.id.favorite_detail))
    }

    private fun setupPageButtons() {
        val pageButtonClickListener = PageButtonClickListener(this, this::class.java, intent.getStringExtra("news_lang") ?: "zh-tw")
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
        findViewById<RecyclerView>(R.id.newView_all).adapter = NewAdapterAll(news)
    }

    class NewAdapterAll(private val news: List<NewsItem>) : RecyclerView.Adapter<NewAdapterAll.NewViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_all, parent, false)
            return NewViewHolder(view)
        }

        override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
            holder.bind(news[position])
        }

        override fun getItemCount(): Int {
            return news.size
        }

        class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private lateinit var newsItem: NewsItem

            init {
                itemView.setOnClickListener {
                    if (::newsItem.isInitialized) {
                        val intent = newsItem.createNewsIntent(itemView.context)
                        itemView.context.startActivity(intent)
                    }
                }
            }

            private fun NewsItem.createNewsIntent(context: Context): Intent {
                return Intent(context, NewsActivity::class.java).apply {
                    putExtra("webview_url", url)
                }
            }

            fun bind(newsItem: NewsItem) {
                this.newsItem = newsItem
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(dateFormat.parse(newsItem.posted))
                val formattedDateModified = dateFormat.format(dateFormat.parse(newsItem.modified))

                with(itemView) {
                    findViewById<TextView>(R.id.newsTitleTextView).text = newsItem.title
                    findViewById<TextView>(R.id.newsDescriptionTextView).text = newsItem.description
                    findViewById<TextView>(R.id.newsPostedTextView).text = formattedDate
                    findViewById<TextView>(R.id.newsModifiedTextView).text = formattedDateModified
                }
            }
        }
    }
}
