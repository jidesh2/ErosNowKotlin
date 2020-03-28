package com.jidesh.erosnowjidesh.ui.main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jidesh.erosnowjidesh.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DetailActivity extends AppCompatActivity {


    private int movieId = -1;
    private MediaStore.Images images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        View v=findViewById(R.id.container);
        ImageView im=findViewById(R.id.imageView);
        View overview=findViewById(R.id.overviewHeader);
        TextView overviewtext=findViewById(R.id.overviewTextView);
       // TextView genre=findViewById(R.id.genresTextView);
        TextView release_date=findViewById(R.id.durationTextView);
        TextView language=findViewById(R.id.languageTextView);
        TextView title=findViewById(R.id.title);
      // View progress=findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           // movieId = extras.getInt(MOVIE_ID);
            String movieTitle = extras.getString("MOVIE_TITLE");

            title.setText(extras.getString("title"));
            overviewtext.setText( extras.getString("desc"));
           // genre.setText( extras.getString("genre"));
            release_date.setText( extras.getString("release_date"));
            language.setText( extras.getString("language"));
            String fullImageUrl = extras.getString("image");
            getSupportActionBar().setTitle(extras.getString("title"));
            if (!fullImageUrl.isEmpty()) {
                Glide.with(this)
                        .load(fullImageUrl)
                        .apply(RequestOptions.centerCropTransform())
                        .transition(withCrossFade())
                        .into(im);
            }
        }







    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
