package com.vishakha.androidTest.udacityapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends BaseAdapter {

    private Context appContext;
    private List<MovieModel> movies;

    MovieAdapter(Context appContext, List<MovieModel> movies) {
        this.appContext = appContext;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieModel movie = getItem(position);
        ImageView imageView = (ImageView) convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageView = (ImageView) inflater.inflate(R.layout.movie_item, parent, false);
        }

        String url = new StringBuilder()
                .append("https://image.tmdb.org/t/p/")
                .append("w500")
                .append(movie.getPosterPath().trim()).toString();

        Picasso.with(appContext)
                .load(url)
                .into(imageView);

        return imageView;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public MovieModel getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
