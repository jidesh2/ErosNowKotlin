package com.jidesh.erosnowjidesh.ui.main.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.ViewHolders.FavFragViewmodel
import com.jidesh.erosnowjidesh.ui.main.Activities.DetailActivity
import com.jidesh.erosnowjidesh.ui.main.Activities.MainActivity
import com.jidesh.erosnowjidesh.ui.main.Adapters.fav_adapter.Fav_moviesclass
import com.jidesh.erosnowjidesh.ui.main.Fragments.PlaceholderFragment
import java.util.*

class fav_adapter(var context: Context) : RecyclerView.Adapter<Fav_moviesclass>() {
    private var movies: List<Fav_movies> = ArrayList()
    private val viewmodel: FavFragViewmodel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Fav_moviesclass {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
        return Fav_moviesclass(itemview)
    }
    init {
        viewmodel = ViewModelProvider((context as MainActivity)).get(FavFragViewmodel::class.java)
    }
    override fun onBindViewHolder(holder: Fav_moviesclass, position: Int) {
        val (id, img) = movies[position]
        //Toast.makeText(context,movie_single.getImg(),Toast.LENGTH_LONG).show();
        Glide
                .with(context)
                .load(BASE_URL_IMG + img)
                .into(holder.mPosterImg)
        if (viewmodel.check(id) == 0) { // result.setSelected(!result.isSelected());
            holder.favourite.setImageResource(R.drawable.ic_nofav)
            //  Log.d("id",result.getId().toString());
        } else { //   result.setSelected(!result.isSelected());
            holder.favourite.setImageResource(R.drawable.ic_favourite)
        }
        holder.favourite.setOnClickListener {
            val tab0Fragment = (context as MainActivity).supportFragmentManager.fragments[0] as PlaceholderFragment
            //   movie_single.setSelected(!result.isSelected());
            tab0Fragment.datasetchange()
            val fav = Fav_movies(id, img)
            viewmodel.delete(fav)
        }
        holder.mPosterImg.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("id", id.toString())

            context.startActivity(intent)
        }
    }

    fun setfavourites(favs: List<Fav_movies>) {
        movies = favs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class Fav_moviesclass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mPosterImg: ImageView
        val favourite: ImageView

        init {
            mPosterImg = itemView.findViewById<View>(R.id.thumbnail) as ImageView
            favourite = itemView.findViewById<View>(R.id.favourite) as ImageView
        }
    }

    companion object {
        private const val BASE_URL_IMG = "https://image.tmdb.org/t/p/w500"
    }


}