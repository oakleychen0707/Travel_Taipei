package com.example.travel_test

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FourthActivity : AppCompatActivity() {

    lateinit var lang: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        supportActionBar?.hide() // 隱藏應用程式本身的ToolBar

        lang = intent.getStringExtra("lang") ?: "zh-tw"

        val favoriteAttractions = FavoritesManager.getFavorites()

        if (favoriteAttractions.isEmpty()) {
            showNoFavoritesDialog()
        } else {
            // 設置 RecyclerView
            val favoriteView = findViewById<RecyclerView>(R.id.favoriteView_all)
            favoriteView.layoutManager = LinearLayoutManager(this)
            favoriteView.adapter = FavoriteAdapter(favoriteAttractions)
        }

        // 更新 Toolbar 的標題文字
        val toolbar = findViewById<TextView>(R.id.toolbar_title)
        val titleMap = mapOf(
            "zh-tw" to "收藏項目",
            "zh-cn" to "收藏项目",
            "en" to "Collection Items",
            "ja" to "コレクションアイテム",
            "ko" to "컬렉션 아이템",
            "es" to "Artículos de colección",
            "th" to "รายการคอลเลกชัน",
            "vi" to "Vật phẩm sưu tập"
        )

        when (lang) {
            "zh-tw", "zh-cn", "en", "ja", "ko", "es", "th", "vi" -> {
                toolbar.text = titleMap[lang]
            }
        }

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("attractions_lang", lang)
            finish()
        }

    }

    private fun showNoFavoritesDialog() {
        AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("請將「旅遊景點」加入收藏")
            .setPositiveButton("確認") { dialog, _ ->
                dialog.dismiss()
                if (!isFinishing) {
                    navigateToThirdPageWithLanguage(lang)
                }
            }
            .setOnDismissListener {
                if (!isFinishing) {
                    navigateToThirdPageWithLanguage(lang)
                }
            }
            .setCancelable(false)
            .show()
    }


    private fun navigateToThirdPageWithLanguage(lang: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("attractions_lang", lang)
        finish()
    }

    class FavoriteAdapter(private val favoriteAttractions: List<Attraction>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attraction, parent, false)
            return FavoriteViewHolder(view)
        }

        override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
            val attraction = favoriteAttractions[position]
            holder.bind(attraction, position)
        }

        override fun getItemCount(): Int {
            return favoriteAttractions.size
        }

        class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val attractionImageView: ImageView = itemView.findViewById(R.id.attractionImageView)
            private val attractionTelTextView: TextView = itemView.findViewById(R.id.attractionTelTextView)
            private val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
            private lateinit var attraction: Attraction

            init {
                itemView.setOnClickListener {
                    if (::attraction.isInitialized) {
                        val intent = attraction.createAttractionIntent(itemView.context)
                        itemView.context.startActivity(intent)
                    }
                }

                favoriteButton.visibility = View.GONE
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

            fun bind(attraction: Attraction, position: Int) {
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
                    // 設為不可見
                    attractionImageView.visibility = View.GONE
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