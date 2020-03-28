package com.jidesh.erosnowjidesh.ViewHolders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;
import com.jidesh.erosnowjidesh.repository.Fav_repository;

import java.util.List;

public class FavFragViewmodel extends AndroidViewModel {
    private Fav_repository repository;
    private LiveData<List<Fav_movies>> allFavMovies;
    public FavFragViewmodel(@NonNull Application application) {
        super(application);
        repository=new Fav_repository(application);
        allFavMovies=repository.getAllFavs();
    }
    public void inset(Fav_movies fav_movies)
    {
        repository.Insert(fav_movies);
    }
    public void delete(Fav_movies fav_movies)
    {
        repository.Delete(fav_movies);
    }
    public LiveData<List<Fav_movies>> getAllFavMovies()
    {
        return allFavMovies;
    }
    public Integer check(int a)
    {
        return   repository.find(a);

    }
}
