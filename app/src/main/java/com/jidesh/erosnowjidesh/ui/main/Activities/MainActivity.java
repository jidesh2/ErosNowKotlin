package com.jidesh.erosnowjidesh.ui.main.Activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import android.util.Log;
import android.widget.ImageView;


import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jidesh.erosnowjidesh.Models.Entities.Movie;
import com.jidesh.erosnowjidesh.R;
import com.jidesh.erosnowjidesh.ViewHolders.PageViewModel;
import com.jidesh.erosnowjidesh.ui.main.Fragments.PlaceholderFragment;
import com.jidesh.erosnowjidesh.ui.main.Adapters.SectionsPagerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PageViewModel pageViewModel;
    PlaceholderFragment tab0Fragment;
    //Fav_repository repository;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
       viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        pageViewModel= new ViewModelProvider(this).get(PageViewModel.class);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
      //  repository=new Fav_repository(getApplication());
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        ImageView searchIcon=
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        // To change color of close button, use:
        // ImageView searchCloseIcon = (ImageView)searchView
        //        .findViewById(androidx.appcompat.R.id.search_close_btn);

        searchIcon.setColorFilter(getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tab0Fragment = (PlaceholderFragment) getSupportFragmentManager().getFragments().get(0);
                tab0Fragment.refresh();
                searchView.setQuery("", false);

                searchView.onActionViewCollapsed();

                searchViewItem.collapseActionView();
                return false;

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                tab0Fragment = (PlaceholderFragment) getSupportFragmentManager().getFragments().get(0);
                Log.d("query",query);
               pageViewModel.search(query);
               pageViewModel.getSearch(query).observe(MainActivity.this, new Observer<List<Movie>>() {
                   @Override
                   public void onChanged(List<Movie> movies) {

//                       Log.d("testmovies",movies.get(0).getId().toString());
                       if(movies.size()==0)
                       {
                           Toast.makeText(MainActivity.this,"No Matching movie found",Toast.LENGTH_LONG).show();
                       }
                       else
                       {
                           viewPager.setCurrentItem(0);
                           tab0Fragment.changedapter(movies);
                       }
                   }

               });

                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {

                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }
}