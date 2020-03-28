package com.jidesh.erosnowjidesh.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.jidesh.erosnowjidesh.Models.Databases.AppDatabase;
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;
import com.jidesh.erosnowjidesh.Models.DAO.FavsDao;
import com.jidesh.erosnowjidesh.Models.Entities.Movie;
import com.jidesh.erosnowjidesh.Models.Entities.MoviesResponse;
import com.jidesh.erosnowjidesh.retrofit.RetrofitClient;
import com.jidesh.erosnowjidesh.retrofit.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fav_repository {
    private FavsDao favsDao;
    private Service movieService;
    private List<Movie> movies;
    List<Movie> results = null;
    private LiveData<List<Fav_movies>> allFavs;


    public Fav_repository(Application application) {
        AppDatabase appdatabse = AppDatabase.getAppDatabase(application);
        favsDao = appdatabse.favsDao();
        allFavs = favsDao.getAllFavs();
        movieService = RetrofitClient.getRetrofitInstance(application).create(Service.class);

    }

    public void Insert(Fav_movies fav) {
        new insertAsync(favsDao).execute(fav);
    }

    public void Delete(Fav_movies fav) {
        new deleteAsync(favsDao).execute(fav);
    }

    public Integer find(int id) {
Integer a=0;
        try {
            a= new findSync(favsDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a;
    }

    public LiveData<List<Fav_movies>> getAllFavs()
    {
        return allFavs;
    }

    private static class insertAsync extends AsyncTask<Fav_movies,Void,Void>{
        private FavsDao favsDao;
    private insertAsync(FavsDao favsDao)
    {
    this.favsDao= favsDao;
    }

        @Override
        protected Void doInBackground(Fav_movies... fav_movies) {
        favsDao.insert(fav_movies[0]);
            return null;
        }
    }
    private static class deleteAsync extends AsyncTask<Fav_movies,Void,Void>{
        private FavsDao favsDao;
        private deleteAsync(FavsDao favsDao)
        {
            this.favsDao= favsDao;
        }

        @Override
        protected Void doInBackground(Fav_movies... fav_movies) {
            favsDao.delete(fav_movies[0]);
            return null;
        }
    }
    private static class findSync extends AsyncTask<Integer, Void, Integer> {
        private FavsDao favsDao;
        private findSync(FavsDao favsDao)
        {
            this.favsDao= favsDao;
        }

        @Override
        protected Integer doInBackground(Integer... fav_movies) {

            return  favsDao.check(fav_movies[0]);
        }
    }
    public MutableLiveData<List<Movie>> getNews(String search){
        MutableLiveData<List<Movie>> newsData = new MutableLiveData<>();

        callSearchMoviesApi(search).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {





                if (response.isSuccessful()){
                    newsData.setValue(fetchResults(response));
                }



            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {


            }
        });
        return newsData;
    }
    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }
    private Call<MoviesResponse> callSearchMoviesApi(String search) {
        return movieService.getSearchedMovies(
                "9b06479415c61ae2a3ae755c53f15f6b",search

        );
    }
}
