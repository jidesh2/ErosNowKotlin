package com.jidesh.erosnowjidesh.ui.main.Fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.Utility.GridSpacingItemDecoration
import com.jidesh.erosnowjidesh.ViewHolders.FavFragViewmodel
import com.jidesh.erosnowjidesh.ui.main.Activities.MainActivity
import com.jidesh.erosnowjidesh.ui.main.Adapters.fav_adapter

class Favourite_fragment :Fragment()
{

    lateinit var  viewmodel: FavFragViewmodel
    var adapter: fav_adapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    var rv: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fav_fragment, container, false)
        rv = view.findViewById<View>(R.id.fav_recycler2) as RecyclerView


        adapter = fav_adapter(context as MainActivity)
        linearLayoutManager = GridLayoutManager(context as MainActivity, 2)

        rv!!.layoutManager = linearLayoutManager
        rv!!.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        rv!!.itemAnimator = DefaultItemAnimator()

        rv!!.adapter = adapter
        viewmodel = ViewModelProvider((context as MainActivity)).get(FavFragViewmodel::class.java)
        viewmodel.allFavMovies?.observe(viewLifecycleOwner, Observer<List<Fav_movies>> { fav_movies ->

            adapter!!.setfavourites(fav_movies)

        })
        return view

    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}