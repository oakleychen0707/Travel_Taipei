package com.example.travel_test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class FragmentAttractions_1 : Fragment() {


    private lateinit var presenter: MainContract.Presenter
    private lateinit var attractionsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lang = arguments?.getString("attractions_lang", "zh-tw") ?: "zh-tw"

        presenter = ThirdPresenter(this)

        if (!NetworkUtils.isNetworkConnected(requireActivity())) {
            NetworkUtils.showNetworkErrorDialog(requireActivity())
        } else {
            presenter.getAttractions(lang)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attractions_1, container, false)
        attractionsView = view.findViewById(R.id.attractionView_all)
        return view
    }

    fun showAttractionsError(message: String) {
        Log.e("API Attractions Call", message)
    }

    fun showAttractions(attractions: List<Attraction>) {
        for (attraction in attractions) {
//            Log.d("Attraction", attraction.name)
//            Log.d("Attraction", attraction.introduction)
//            Log.d("Language Selected", "Selected Language: $lang")
        }
        attractionsView.adapter = AttractionAdapter_all(attractions)
    }


    class AttractionAdapter_all(private val attractions: List<Attraction>) : RecyclerView.Adapter<AttractionAdapter_all.AttractionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attraction, parent, false)
            return AttractionViewHolder(view)
        }

        override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
            val attraction = attractions[position]
            holder.bind(attraction)
        }

        override fun getItemCount(): Int {
            return attractions.size
        }

        class AttractionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val attractionImageView: ImageView = itemView.findViewById(R.id.attractionImageView)
            private val attractionTelTextView: TextView = itemView.findViewById(R.id.attractionTelTextView)
            private val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
            private lateinit var attraction: Attraction
            private var isFavorite: Boolean = false

            init {
                itemView.setOnClickListener {
                    println("Attractions 點擊了")
                    if (::attraction.isInitialized) {
                        println("初始化")
                        val intent = attraction.createAttractionIntent(itemView.context)
                        itemView.context.startActivity(intent)
                    }
                }

                favoriteButton.setOnClickListener {
                    isFavorite = !isFavorite
                    Log.d("FavoriteButton", "isFavorite: $isFavorite")

                    // 設置收藏圖標
                    val favoriteIcon = if (isFavorite) R.drawable.heartred_wrapper else R.drawable.heart_wrapper
                    Log.d("FavoriteButton", "Setting favorite icon: $favoriteIcon")
                    favoriteButton.setImageResource(favoriteIcon)

                    // 打印收藏的項目信息
                    val message = if (isFavorite) "已收藏項目: ${attraction.name}" else "取消收藏項目: ${attraction.name}"
                    Log.d("FavoriteButton", message)

                    // 更新收藏列表
                    if (isFavorite) {
                        FavoritesManager.addFavorite(attraction)
                    } else {
                        FavoritesManager.removeFavorite(attraction)
                    }
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
                    // 設為不可見
                    attractionImageView.visibility = View.GONE
                }

                // 檢查是否有電話號碼
                if (attraction.tel.isEmpty()) {
                    // 如果電話號碼為空，則設為不可見
                    attractionTelTextView.visibility = View.GONE
                }
                // 設置收藏圖標
                val favoriteIcon = if (isFavorite) R.drawable.heartred_wrapper else R.drawable.heart_wrapper
                favoriteButton.setImageResource(favoriteIcon)
            }
        }
    }
}
