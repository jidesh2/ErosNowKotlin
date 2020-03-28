package com.jidesh.erosnowjidesh.ui.main.Adapters;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.jidesh.erosnowjidesh.ui.main.Activities.DetailActivity;
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;
import com.jidesh.erosnowjidesh.ViewHolders.PageViewModel;
import com.jidesh.erosnowjidesh.repository.Fav_repository;
import com.jidesh.erosnowjidesh.Models.Entities.Movie;
import com.jidesh.erosnowjidesh.R;


import java.util.ArrayList;
import java.util.List;



public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Fav_repository repository;
   int  row_index=-1;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    ScaleAnimation scaleAnimation;
    BounceInterpolator bounceInterpolator;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";
    private PageViewModel viewmodel;
    private List<Movie> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context, PageViewModel page) {
        this.context = context;
        movieResults = new ArrayList<>();
        viewmodel= page;
    }

    public List<Movie> getMovies() {
        return movieResults;
    }

    public void setMovies(List<Movie> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_movies, parent, false);

        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Movie result = movieResults.get(position);
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
               // ((MovieVH) holder).favourite.setText(result.isSelected() ? "Remove From Favourite" : "Add To Favourites");
                if (viewmodel.check(result.getId()) == 0) {
                   // result.setSelected(!result.isSelected());
                    ((MovieVH) holder).favourite.setImageResource(R.drawable.ic_nofav);
                    //  Log.d("id",result.getId().toString());



                } else
                {
                 //   result.setSelected(!result.isSelected());
                    ((MovieVH) holder).favourite.setImageResource(R.drawable.ic_favourite);

                }
                Glide
                        .with(context)
                        .load(BASE_URL_IMG + result.getPosterPath())
                        .into(movieVH.mPosterImg);
                ((MovieVH) holder).favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (viewmodel.check(result.getId()) == 0&& !result.isSelected()) {
                            result.setSelected(!result.isSelected());
                            ((MovieVH) holder).favourite.setImageResource(R.drawable.ic_favourite);
                            ((MovieVH) holder).favourite.startAnimation(scaleAnimation);
                          //  Log.d("id",result.getId().toString());
                            Fav_movies fav = new Fav_movies(result.getId(), result.getPosterPath());
                            viewmodel.inset(fav);



                        } else
                        {
                            result.setSelected(!result.isSelected());
                           ((MovieVH) holder).favourite.setImageResource(R.drawable.ic_nofav);
                            ((MovieVH) holder).favourite.startAnimation(scaleAnimation);
                            Fav_movies fav = new Fav_movies(result.getId(), result.getPosterPath());
                            viewmodel.delete(fav);

                        }
                   //    notifyDataSetChanged();
                    }

                });
                ((MovieVH) holder).mPosterImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in= new Intent(context, DetailActivity.class);
                        in.putExtra("title",result.getTitle());
                        in.putExtra("image",BASE_URL_IMG + result.getPosterPath());
                        in.putExtra("desc",result.getOverview());
                        in.putExtra("release_date",result.getReleaseDate());

                        in.putExtra("language",result.getOriginalLanguage());
                        context.startActivity(in);
                    }
                });
                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }
    public int getItemPosition(long id)
    {
        for (int position=0; position<movieResults.size(); position++)
            if (movieResults.get(position).getId() == id)
                return position;
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Movie r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void remove(Movie r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }


        public void clear () {
            int size = movieResults.size();
            movieResults.clear();
            notifyItemRangeRemoved(0, size);


    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movieResults.get(position);
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private ImageView favourite;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);

            mPosterImg = (ImageView) itemView.findViewById(R.id.thumbnail);
            favourite=(ImageView) itemView.findViewById(R.id.favourite);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}