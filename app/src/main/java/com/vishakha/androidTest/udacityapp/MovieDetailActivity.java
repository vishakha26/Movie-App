package com.vishakha.androidTest.udacityapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {
    private ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView mTitle, mRelease, mRating, mDescription;
        MovieModel movies = getIntent().getParcelableExtra(getString(R.string.movie));


        mTitle = (TextView) findViewById(R.id.title);
        mRelease = (TextView) findViewById(R.id.release);
        mRating = (TextView) findViewById(R.id.rating);
        mDescription = (TextView) findViewById(R.id.description);
        mPoster = (ImageView) findViewById(R.id.poster);


        String urlBuilder = new StringBuilder()
                .append(getString(R.string.baseURL))
                .append(getString(R.string.ImageSize))
                .append(movies.getPosterPath()).toString();

        Picasso.with(getApplicationContext())
                .load(urlBuilder)
                .into(mPoster);

        mTitle.setText(movies.getTitle());
        mRelease.setText(getResources().getString(R.string.release) + " " + movies.getReleaseDate());
        mRating.setText(getResources().getString(R.string.rating) + " " + movies.getUserRating() + "/10");
        mDescription.setText(getResources().getString(R.string.overview) + movies.getOverview());

   }

}
