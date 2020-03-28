package com.jidesh.erosnowjidesh.ui.main.Fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jidesh.erosnowjidesh.ViewHolders.FavFragViewmodel;
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;
import com.jidesh.erosnowjidesh.Utility.GridSpacingItemDecoration;
import com.jidesh.erosnowjidesh.ui.main.Activities.MainActivity;
import com.jidesh.erosnowjidesh.R;
import com.jidesh.erosnowjidesh.retrofit.Service;
import com.jidesh.erosnowjidesh.ui.main.Adapters.fav_adapter;

import java.util.ArrayList;
import java.util.List;

public class Favourite_fragment extends Fragment {

private FavFragViewmodel viewmodel;
    fav_adapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private Service movieService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<Fav_movies> fav_movies=new ArrayList<>();
        View view= inflater.inflate(R.layout.fav_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.fav_recycler2);


        adapter = new fav_adapter(getActivity());
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        // recyclerView.setLayoutManager(mLayoutManager);
        //  linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        viewmodel= new ViewModelProvider((MainActivity)getContext()).get(FavFragViewmodel.class);
viewmodel.getAllFavMovies().observe(getViewLifecycleOwner(), new Observer<List<Fav_movies>>() {
    @Override
    public void onChanged(List<Fav_movies> fav_movies) {
//update Recycerview
        adapter.setfavourites(fav_movies);
//        Toast.makeText(getContext(),fav_movies.get(0).getImg(),Toast.LENGTH_LONG).show();
    }
});
        return view;
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
