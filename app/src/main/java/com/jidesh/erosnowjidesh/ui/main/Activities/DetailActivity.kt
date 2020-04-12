package com.jidesh.erosnowjidesh.ui.main.Activities

import android.content.ContentValues
import android.content.DialogInterface
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.Utility.MyReachability
import com.jidesh.erosnowjidesh.retrofit.RetrofitClient
import com.jidesh.erosnowjidesh.retrofit.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity :AppCompatActivity()
{
    private var movieId = "0"
    private val BASE_URL_IMG = "https://image.tmdb.org/t/p/w500"
    private var MOVIE_ID:String ="id"
    private var movieService: Service? = null
   lateinit var v : View
    lateinit  var im : ImageView
    lateinit var overview : View
   lateinit var overviewtext : TextView
   lateinit var release_date : TextView
    lateinit var language : TextView
   lateinit var title : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        movieService = RetrofitClient.getRetrofitInstance(baseContext)!!.create(Service::class.java)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
         v = findViewById<View>(R.id.container) as View
        im = findViewById<ImageView>(R.id.imageView) as ImageView
         overview = findViewById<View>(R.id.overviewHeader) as View
     overviewtext = findViewById<TextView>(R.id.overviewTextView) as TextView
      release_date = findViewById<TextView>(R.id.durationTextView) as TextView
         language = findViewById<TextView>(R.id.languageTextView) as TextView
         title = findViewById<TextView>(R.id.title) as TextView

      //  val extras = intent.extras
        val extras=intent.getStringExtra("id")
        if (extras != null) {
             movieId = extras
        }
        val loader = Thread {
            when {

                MyReachability.hasInternetConnected(baseContext) -> runOnUiThread {
                    loadMovie()
                    //  pageViewModel!!.search(query)
                }
                else -> runOnUiThread {

                    showInternetConnectionLost()
                }
            }
        }
        loader.start()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun loadMovie() {
        Log.d(ContentValues.TAG, "loadFirstPage: ")
        callMoviewById().enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if(response.isSuccessful) {
                    title.setText(response.body()!!.originalTitle)
                    overviewtext.setText(response.body()!!.overview)
                    // genre.setText( extras.getString("genre"));
                    // genre.setText( extras.getString("genre"));
                    release_date.setText(response.body()!!.releaseDate)
                    language.setText(response.body()!!.originalLanguage)

                    supportActionBar!!.setTitle(response.body()!!.originalTitle)
                    if (!response.body()!!.posterPath.isNullOrBlank()) {
                        Glide.with(baseContext)
                                .load(BASE_URL_IMG+response.body()!!.posterPath)
                                .apply(RequestOptions.centerCropTransform())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(im)
                    }

                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Toast.makeText(baseContext, "Please turn on internet", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun callMoviewById(): Call<Movie> {
        return movieService!!.getMovieById(
                movieId.toString(),
                "9b06479415c61ae2a3ae755c53f15f6b"

        )
    }
    fun showInternetConnectionLost(){
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
loadMovie()
                        }
                    }else{          Handler(Looper.getMainLooper()).postDelayed({showInternetConnectionLost() }, 500)
                    }
                }
            })
            dialog.show()
        } catch (e: Exception) {
            Log.e(MyReachability.classTag, e.toString())
        }
    }
}