package com.jidesh.erosnowjidesh.ui.main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jidesh.erosnowjidesh.ViewHolders.FavFragViewmodel;
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;
import com.jidesh.erosnowjidesh.ui.main.Activities.MainActivity;
import com.jidesh.erosnowjidesh.R;
import com.jidesh.erosnowjidesh.ui.main.Fragments.PlaceholderFragment;

import java.util.ArrayList;
import java.util.List;

public class fav_adapter extends RecyclerView.Adapter<fav_adapter.Fav_moviesclass> {
    private List<Fav_movies> movies=new ArrayList<>();
    Context context;
private FavFragViewmodel viewmodel;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";
    @NonNull
    @Override
    public Fav_moviesclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies,parent,false);
        return new Fav_moviesclass(itemview);
    }
    public fav_adapter(Context context) {
        this.context = context;
        viewmodel = new ViewModelProvider((MainActivity)context).get(FavFragViewmodel.class);

    }

    @Override
    public void onBindViewHolder(@NonNull Fav_moviesclass holder, int position) {
Fav_movies movie_single=movies.get(position);
//Toast.makeText(context,movie_single.getImg(),Toast.LENGTH_LONG).show();
        Glide
                .with(context)
                .load(BASE_URL_IMG + movie_single.getImg())
                .into(holder.mPosterImg);

        if (viewmodel.check(movie_single.getId())==0) {
            // result.setSelected(!result.isSelected());
             holder.favourite.setImageResource(R.drawable.ic_nofav);
            //  Log.d("id",result.getId().toString());



        } else
        {
            //   result.setSelected(!result.isSelected());
            holder.favourite.setImageResource(R.drawable.ic_favourite);

        }
    holder.favourite.setOnClickListener(new View.OnClickListener() {
             @Override
         public void onClick(View view) {
                 PlaceholderFragment tab0Fragment = (PlaceholderFragment) ((MainActivity) context).getSupportFragmentManager().getFragments().get(0);
              //   movie_single.setSelected(!result.isSelected());
                tab0Fragment.datasetchange();
                 Fav_movies fav = new Fav_movies(movie_single.getId(), movie_single.getImg());
                 viewmodel.delete(fav);
            }
});

    }

public void setfavourites(List<Fav_movies> favs)
{
    this.movies=favs;
notifyDataSetChanged();
}

    @Override
    public int getItemCount() {
        return movies.size();
    }
    protected class Fav_moviesclass extends RecyclerView.ViewHolder {

        private ImageView mPosterImg;
        private ImageView favourite;
        public Fav_moviesclass(View itemView) {
            super(itemView);
            mPosterImg = (ImageView) itemView.findViewById(R.id.thumbnail);
            favourite=(ImageView)itemView.findViewById(R.id.favourite);
        }
    }


}
