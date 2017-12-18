package com.vishakha.androidTest.udacityapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private String sortBy = "popular";
    private MovieAdapter movieAdapter;
    private ArrayList<MovieModel> movieList;
    GridView gridView;
    static TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);

        error = (TextView) findViewById(R.id.error);

        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieModel movie = movieAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(getString(R.string.movie), movie);
                startActivity(intent);
            }
        });

        getMovieData(sortBy);
    }

    private void getMovieData(String sort) {

        Toast.makeText(this, getString(R.string.message), Toast.LENGTH_SHORT).show();

        error.setVisibility(View.INVISIBLE);

        FetchMovieTask moviesTask = new FetchMovieTask(new TaskCompleted() {
            @Override
            public void movieCompleted(MovieModel[] movies) {
                if (movies != null) {
                    if(movieList.size()>0) {
                        movieList.clear();
                    }
                    Collections.addAll(movieList, movies);
                    movieAdapter.notifyDataSetChanged();
                }

            }
        });

        moviesTask.execute(sort);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idSelected = item.getItemId();

        if(isNetworkAvailable()) {
            if (idSelected == R.id.action_top_rated) {
                sortBy = "top_rated";
                getMovieData(sortBy);
                Toast.makeText(this,R.string.ratingMessage,Toast.LENGTH_SHORT).show();
                return true;
            }

            if (idSelected == R.id.action_most_popular) {
                sortBy = "popular";
                getMovieData(sortBy);
                Toast.makeText(this,R.string.popularityMessage,Toast.LENGTH_SHORT).show();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
        else {
            error.setVisibility(View.VISIBLE);
        }
        return false;
    }
}
