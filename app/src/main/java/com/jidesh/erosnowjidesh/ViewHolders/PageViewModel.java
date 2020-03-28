package com.jidesh.erosnowjidesh.ViewHolders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;
import com.jidesh.erosnowjidesh.repository.Fav_repository;
import com.jidesh.erosnowjidesh.Models.Entities.Movie;

import java.util.List;

public class PageViewModel extends AndroidViewModel {
    private Fav_repository repository;
    private LiveData<List<Fav_movies>> allFavMovies;
    private MutableLiveData<List<Movie>> mutableLiveData;
    public PageViewModel(@NonNull Application application) {
        super(application);
        repository=new Fav_repository(application);
        allFavMovies=repository.getAllFavs();


    }
    public LiveData<List<Movie>> getSearch(String s) {

            mutableLiveData = new MutableLiveData<List<Movie>>();

          mutableLiveData=  repository.getNews(s);

        return mutableLiveData;
    }

    public void inset(Fav_movies fav_movies)
    {
        repository.Insert(fav_movies);
    }
    public void delete(Fav_movies fav_movies)
    {
        repository.Delete(fav_movies);
    }
    public void search(String search){repository.getNews(search);}
    public Integer check(int a)
    {
      return   repository.find(a);

    }

}