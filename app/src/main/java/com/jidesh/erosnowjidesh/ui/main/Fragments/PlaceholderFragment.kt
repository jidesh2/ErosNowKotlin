package com.jidesh.erosnowjidesh.ui.main.Fragments

import android.content.ContentValues
import android.content.DialogInterface
import android.os.*
import android.provider.SyncStateContract
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.Models.Entities.MoviesResponse
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.Utility.GridSpacingItemDecoration
import com.jidesh.erosnowjidesh.Utility.MyReachability
import com.jidesh.erosnowjidesh.Utility.MyReachability.classTag
import com.jidesh.erosnowjidesh.Utility.PaginationScrollListener
import com.jidesh.erosnowjidesh.ViewHolders.PageViewModel
import com.jidesh.erosnowjidesh.retrofit.RetrofitClient.getRetrofitInstance
import com.jidesh.erosnowjidesh.retrofit.Service
import com.jidesh.erosnowjidesh.ui.main.Activities.MainActivity
import com.jidesh.erosnowjidesh.ui.main.Adapters.PaginationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceholderFragment  :Fragment(){
    var  adapter:PaginationAdapter?=null
   lateinit var linearLayoutManager: LinearLayoutManager
   lateinit var  pageViewModel: PageViewModel
   lateinit var rv: RecyclerView
 //  private var progressBar: ProgressBar?=null
    private var PAGE_START = 1
    private var isLoading_this = false
    private var isLastPage_this = false

   var totalPageCount_this = 15

    private var currentPage = PAGE_START

    private var movieService: Service? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        rv=root.findViewById(R.id.main_recycler) as RecyclerView
     //  progressBar=root.findViewById(R.id.progressBar) as? ProgressBar

        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
        adapter = PaginationAdapter(activity, pageViewModel)
        linearLayoutManager =GridLayoutManager(activity,2);
        rv.setLayoutManager(linearLayoutManager)
        rv.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        rv.setItemAnimator(DefaultItemAnimator())

        rv.setAdapter(adapter)

        movieService = getRetrofitInstance(activity)!!.create(Service::class.java)

        rv.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading_this = true
                currentPage += 1
                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override var totalPageCount: Int=15
                get() = totalPageCount_this

            override var isLastPage: Boolean=false
                get() = isLastPage_this

            override var isLoading: Boolean=false
                get()= isLoading_this
        })


        loadFirstPage()

        return root
    }
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
    fun loadFirstPage() {
        val loader = Thread {
            when {

                MyReachability.hasInternetConnected(context as MainActivity) -> (context as MainActivity).runOnUiThread {
                    Log.d(ContentValues.TAG, "loadFirstPage: ")
                    callPopularMoviesApi().enqueue(object : Callback<MoviesResponse> {
                        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) { // Got data. Send it to adapter


                                        val results = fetchResults(response)
                                        adapter!!.addAll(results)


                                        if (currentPage <= totalPageCount_this) adapter!!.addLoadingFooter() else isLastPage_this= true



                        }

                        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                            showInternetConnectionLost("first")
                        }
                    })
                }
                else -> (context as MainActivity).runOnUiThread {

                    showInternetConnectionLost("first")
                }
            }
        }
        loader.start()

    }

    private fun fetchResults(response: Response<MoviesResponse>): List<Movie> {
        val topRatedMovies = response.body()
        return topRatedMovies!!.getResults()
    }

    private fun loadNextPage() {
        val loader = Thread {
            when {

                MyReachability.hasInternetConnected(context as MainActivity) -> (context as MainActivity).runOnUiThread {
                    Log.d(ContentValues.TAG, "loadNextPage: $currentPage")
                    callPopularMoviesApi().enqueue(object : Callback<MoviesResponse> {
                        override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {

                            adapter!!.removeLoadingFooter()
                            isLoading_this = false
                            val results = fetchResults(response)
                            adapter!!.addAll(results)
                            if (currentPage != totalPageCount_this) adapter!!.addLoadingFooter() else isLastPage_this = true


                        }

                        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                            t.printStackTrace()
                            Toast.makeText(context,"No Internet Please Try again later",Toast.LENGTH_LONG).show()
                            //   adapter!!.removeLoadingFooter()
                            //    isLoading_this = false
                            showInternetConnectionLost("next")
                        }
                    })
                }
                else -> (context as MainActivity).runOnUiThread {

                    showInternetConnectionLost("next")
                }
            }
        }
        loader.start()

    }

    private fun callPopularMoviesApi(): Call<MoviesResponse> {
        return movieService!!.getPopularMovies(
                "9b06479415c61ae2a3ae755c53f15f6b",
                currentPage
        )
    }
    fun changedapter(movie: List<Movie?>){
        adapter!!.clear()
        adapter!!.notifyDataSetChanged()
        adapter!!.addAll(movie)
        adapter!!.notifyDataSetChanged()
        adapter!!.removeLoadingFooter()


    }
    fun refresh()
    {
        adapter!!.clear()

        adapter!!.notifyDataSetChanged()
        currentPage = 1
        loadFirstPage()
        adapter!!.notifyDataSetChanged()
    }
    fun datasetchange() {
        adapter!!.notifyDataSetChanged()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showInternetConnectionLost(a:String){
        try {
            val dialog = AlertDialog.Builder(context as MainActivity, android.R.style.Theme_Material_Dialog_NoActionBar).create()

            dialog.setTitle("No Internet Connection")
            dialog.setMessage("Please make sure your Wi-Fi or mobile data is turned on, then try again.")
            dialog.setCancelable(false)
            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", DialogInterface.OnClickListener { _, _ -> activity!!.finish() })
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Retry", DialogInterface.OnClickListener { _, _ ->
                val hThread = HandlerThread("connectivity")
                hThread.start()
                Handler(hThread.looper).post {
                    if (MyReachability.hasInternetConnected(context as MainActivity)){
                        Handler(Looper.getMainLooper()).post{
                            dialog.dismiss()
                            if(a.equals("next"))
                            {
                                loadNextPage()
                            }
                            else
                            {loadFirstPage()}

                        }
                    }else{          Handler(Looper.getMainLooper()).postDelayed({if (a.equals("next")) showInternetConnectionLost("next") else showInternetConnectionLost("first")}, 500)
                    }
                }
            })
            dialog.show()
        } catch (e: Exception) {
            Log.e(classTag, e.toString())
        }
    }
}