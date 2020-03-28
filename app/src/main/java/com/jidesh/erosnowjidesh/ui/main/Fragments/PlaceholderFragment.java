package com.jidesh.erosnowjidesh.ui.main.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jidesh.erosnowjidesh.Utility.GridSpacingItemDecoration;
import com.jidesh.erosnowjidesh.Models.Entities.Movie;
import com.jidesh.erosnowjidesh.Models.Entities.MoviesResponse;
import com.jidesh.erosnowjidesh.Utility.PaginationScrollListener;
import com.jidesh.erosnowjidesh.R;
import com.jidesh.erosnowjidesh.ViewHolders.PageViewModel;
import com.jidesh.erosnowjidesh.retrofit.RetrofitClient;
import com.jidesh.erosnowjidesh.retrofit.Service;
import com.jidesh.erosnowjidesh.ui.main.Adapters.PaginationAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class PlaceholderFragment extends Fragment {
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
private PageViewModel pageViewModel;
    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private Service movieService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PaginationAdapter(getActivity(),pageViewModel);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        rv = (RecyclerView) root.findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) root.findViewById(R.id.main_progress);
        pageViewModel= new ViewModelProvider(this).get(PageViewModel.class);
        adapter = new PaginationAdapter(getActivity(),pageViewModel);

     linearLayoutManager = new GridLayoutManager(getActivity(), 2);

        rv.setLayoutManager(linearLayoutManager);
rv.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        movieService = RetrofitClient.getRetrofitInstance(getActivity()).create(Service.class);
        loadFirstPage();


        return root;
    }



    public void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        callPopularMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                List<Movie> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getActivity(),"Please turn on internet",Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * @param response extracts List<{@link Movie>} from response
     * @return
     */
    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callPopularMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Movie> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(),"Please turn on internet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<MoviesResponse> callPopularMoviesApi() {
        return movieService.getPopularMovies(
                "9b06479415c61ae2a3ae755c53f15f6b",
                currentPage
        );
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public void changedapter(List<Movie>movie)
    {
       Log.d("iam running ","iam running");
     //  loadFirstPage();
        adapter.clear();

        adapter.notifyDataSetChanged();
adapter.addAll(movie);

adapter.notifyDataSetChanged();
        adapter.removeLoadingFooter();

    }
    public void refresh()
    {

        adapter.clear();

        adapter.notifyDataSetChanged();
        currentPage=1;
       loadFirstPage();
        adapter.notifyDataSetChanged();

    }
    public void datasetchange()
    {

        adapter.notifyDataSetChanged();

    }
}