package com.jidesh.erosnowjidesh.ui.main.Activities

import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.Utility.MyReachability
import com.jidesh.erosnowjidesh.ViewHolders.PageViewModel
import com.jidesh.erosnowjidesh.ui.main.Adapters.SectionsPagerAdapter
import com.jidesh.erosnowjidesh.ui.main.Fragments.PlaceholderFragment

class MainActivity : AppCompatActivity() {
    private var pageViewModel: PageViewModel? = null
    var tab0Fragment: PlaceholderFragment? = null

  private var viewPager: ViewPager? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager!!.setAdapter(sectionsPagerAdapter)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem = menu.findItem(R.id.app_bar_search)
        //  repository=new Fav_repository(getApplication());
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        // To change color of close button, use:
// ImageView searchCloseIcon = (ImageView)searchView
//        .findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(resources.getColor(R.color.white),
                PorterDuff.Mode.SRC_IN)
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnCloseListener {
            tab0Fragment = supportFragmentManager.fragments[0] as PlaceholderFragment
            tab0Fragment!!.refresh()
            searchView.setQuery("", false)
            searchView.onActionViewCollapsed()
            searchViewItem.collapseActionView()
            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onQueryTextSubmit(query: String): Boolean {
                tab0Fragment = supportFragmentManager.fragments[0] as PlaceholderFragment

                val loader = Thread {
                    when {

                        MyReachability.hasInternetConnected(baseContext) -> runOnUiThread {
                            pageViewModel!!.getSearch(query).observe(this@MainActivity, Observer<List<Movie?>> { movies ->
                                //                       Log.d("testmovies",movies.get(0).getId().toString());
                                if (movies.size == 0) {
                                    Toast.makeText(this@MainActivity, "No Matching movie found", Toast.LENGTH_LONG).show()
                                } else {
                                    viewPager!!.currentItem = 0
                                    tab0Fragment!!.changedapter(movies)
                                }
                            })
                          //  pageViewModel!!.search(query)
                        }
                        else -> runOnUiThread {
                            Log.d("query", query)
                           showInternetConnectionLost(query)
                        }
                    }
                }
                loader.start()


                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun showInternetConnectionLost(query: String){
        try {
            val dialog = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar).create()

            dialog.setTitle("No Internet Connection")
            dialog.setMessage("Please make sure your Wi-Fi or mobile data is turned on, then try again.")
            dialog.setCancelable(false)
            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Cancel", DialogInterface.OnClickListener { _, _ -> finish() })
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Retry", DialogInterface.OnClickListener { _, _ ->
                val hThread = HandlerThread("connectivity")
                hThread.start()
                Handler(hThread.looper).post {
                    if (MyReachability.hasInternetConnected(this)){
                        Handler(Looper.getMainLooper()).post{
                            dialog.dismiss()

                          // pageViewModel!!.search(query)
                            pageViewModel!!.getSearch(query).observe(this@MainActivity, Observer<List<Movie?>> { movies ->

                                if (movies.size == 0) {
                                    Toast.makeText(this@MainActivity, "No Matching movie found", Toast.LENGTH_LONG).show()
                                } else {
                                    viewPager!!.currentItem = 0
                                    tab0Fragment!!.changedapter(movies)
                                }
                            })
                        }
                    }else{          Handler(Looper.getMainLooper()).postDelayed({showInternetConnectionLost(query) }, 500)
                    }
                }
            })
            dialog.show()
        } catch (e: Exception) {
            Log.e(MyReachability.classTag, e.toString())
        }
    }
}