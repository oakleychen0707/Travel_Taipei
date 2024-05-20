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
            // Log.d("Attraction", attraction.name)
            // Log.d("Attraction", attraction.introduction)
            // Log.d("Language Selected", "Selected Language: $lang")
        }
        attractionsView.adapter = AttractionAdapter_all(attractions)
    }

    class AttractionAdapter_all(private val attractions: List<Attraction>) : RecyclerView.Adapter<AttractionAdapter_all.AttractionViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attraction, parent, false)
            return AttractionViewHolder(view)
        }

        override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
            holder.bind(attractions[position])
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
                setupClickListeners()
            }

            private fun setupClickListeners() {
                itemView.setOnClickListener {
                    if (::attraction.isInitialized) {
                        val intent = attraction.createAttractionIntent(itemView.context)
                        itemView.context.startActivity(intent)
                    }
                }

                favoriteButton.setOnClickListener {
                    isFavorite = !isFavorite
                    updateFavoriteButtonState()
                }
            }

            private fun updateFavoriteButtonState() {
                val favoriteIcon = if (isFavorite) R.drawable.heartred_wrapper else R.drawable.heart_wrapper
                favoriteButton.setImageResource(favoriteIcon)
                val message = if (isFavorite) "已收藏項目: ${attraction.name}" else "取消收藏項目: ${attraction.name}"
                Log.d("FavoriteButton", message)
                updateFavoritesList()
            }

            private fun updateFavoritesList() {
                if (isFavorite) {
                    FavoritesManager.addFavorite(attraction)
                } else {
                    FavoritesManager.removeFavorite(attraction)
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
                bindAttractionDetails()
                bindAttractionImage()
                bindFavoriteButton()
            }

            private fun bindAttractionDetails() {
                with(itemView) {
                    findViewById<TextView>(R.id.attractionNameTextView).text = attraction.name
                    findViewById<TextView>(R.id.attractionAddressTextView).text = attraction.address
                    findViewById<TextView>(R.id.attractionTelTextView).text = attraction.tel
                }

                if (attraction.tel.isEmpty()) {
                    attractionTelTextView.visibility = View.GONE
                }
            }

            private fun bindAttractionImage() {
                if (attraction.images.isNotEmpty()) {
                    val imageUrl = attraction.images[0].src
                    Picasso.get().load(imageUrl).into(attractionImageView)
                } else {
                    attractionImageView.setImageDrawable(null)
                    attractionImageView.visibility = View.GONE
                }
            }

            private fun bindFavoriteButton() {
                val favoriteIcon = if (isFavorite) R.drawable.heartred_wrapper else R.drawable.heart_wrapper
                favoriteButton.setImageResource(favoriteIcon)
            }
        }
    }
}

