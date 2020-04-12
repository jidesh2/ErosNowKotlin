package com.jidesh.erosnowjidesh.ui.main.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies
import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.ViewHolders.PageViewModel
import com.jidesh.erosnowjidesh.repository.Fav_repository
import com.jidesh.erosnowjidesh.ui.main.Activities.DetailActivity
import java.util.*

class PaginationAdapter(private val context: Context?, page: PageViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val repository: Fav_repository? = null
    var row_index = -1
    private var ITEM = 0
    private var LOADING = 1
    var scaleAnimation: ScaleAnimation? = null
    var bounceInterpolator: BounceInterpolator? = null
    private val BASE_URL_IMG = "https://image.tmdb.org/t/p/w500"
 lateinit var  viewmodel: PageViewModel
    private var movieResults: MutableList<Movie>? = null


    private var isLoadingAdded = false
    init {
        movieResults = ArrayList()
        this.viewmodel=page
    }


    fun getMovies(): List<Movie?>? {
        return movieResults
    }

    fun setMovies(movieResults: MutableList<Movie>?) {
        this.movieResults = movieResults
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       lateinit var viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val v2 = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }
        return viewHolder
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.item_movies, parent, false)
        viewHolder = MovieVH(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = movieResults!![position]
        scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation!!.duration = 500
        bounceInterpolator = BounceInterpolator()
        scaleAnimation!!.interpolator = bounceInterpolator
        when (getItemViewType(position)) {
            ITEM -> {
                val movieVH = holder as MovieVH

                if (viewmodel!!.check(result.id!!) == 0) { // result.setSelected(!result.isSelected());
                    holder.favourite.setImageResource(R.drawable.ic_nofav)
                    //  Log.d("id",result.getId().toString());
                } else { //   result.setSelected(!result.isSelected());
                    holder.favourite.setImageResource(R.drawable.ic_favourite)
                }
                Glide
                        .with(context!!)
                        .load(BASE_URL_IMG + result.posterPath)
                        .into(movieVH.mPosterImg)
                holder.favourite.setOnClickListener {
                    if (viewmodel.check(result.id!!) == 0 && !result.isSelected) {
                        result.isSelected = !result.isSelected
                        holder.favourite.setImageResource(R.drawable.ic_favourite)
                        holder.favourite.startAnimation(scaleAnimation)
                        //  Log.d("id",result.getId().toString());
                        val fav = Fav_movies(result.id!!, result.posterPath!!)
                        viewmodel.inset(fav)
                    } else {
                        result.isSelected = !result.isSelected
                        holder.favourite.setImageResource(R.drawable.ic_nofav)
                        holder.favourite.startAnimation(scaleAnimation)
                        val fav = Fav_movies(result.id!!, result.posterPath!!)
                        viewmodel.delete(fav)
                    }
                    //    notifyDataSetChanged();
                }
                holder.mPosterImg.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("id", result.id.toString())

                    context.startActivity(intent)
                }
            }
            LOADING -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (movieResults == null) 0 else movieResults!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movieResults!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(r: Movie) {
        movieResults!!.add(r)
        notifyItemInserted(movieResults!!.size - 1)
    }

    fun addAll(moveResults: List<Movie?>) {
        for (result in moveResults) {
            add(result!!)
        }
    }

    fun remove(r: Movie?) {
        val position = movieResults!!.indexOf(r)
        if (position > -1) {
            movieResults!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun clear() {
        val size = movieResults!!.size
        movieResults!!.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Movie())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieResults!!.size - 1
        val result = getItem(position)
        if (result != null) {
            movieResults!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Movie? {
        return movieResults!![position]
    }


    protected class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favourite: ImageView
        private val mMovieDesc: TextView? = null
        private val mYear // displays "year | language"
                : TextView? = null
        val mPosterImg: ImageView
        private val mProgress: ProgressBar? = null

        init {
            mPosterImg = itemView.findViewById<View>(R.id.thumbnail) as ImageView
            favourite = itemView.findViewById<View>(R.id.favourite) as ImageView
        }
    }


    protected class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

}