package com.vishakha.androidTest.udacityapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class FetchMovieTask extends AsyncTask<String, Void, MovieModel[]> {

    //TODO Enter your api key here
    public static final String API_KEY = "e3062fe8ab0aced5b458cd7d8ec4f3f6";
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


    private final TaskCompleted listener;

    FetchMovieTask(TaskCompleted taskCompleted) {
        this.listener = taskCompleted;
    }

    private MovieModel[] getMoviesJson(String jsonMovieData) throws JSONException {
        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        if(jsonMovieData!=null) {

            JSONObject moviesJson = new JSONObject(jsonMovieData);
            JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);

            MovieModel[] movies = new MovieModel[resultsArray.length()];

            for (int i = 0; i < resultsArray.length(); i++) {
                movies[i] = new MovieModel();

                JSONObject movieInfo = resultsArray.getJSONObject(i);

                movies[i].setTitle(movieInfo.getString(ORIGINAL_TITLE));
                movies[i].setPosterPath(movieInfo.getString(POSTER_PATH));
                movies[i].setOverview(movieInfo.getString(OVERVIEW));
                movies[i].setUserRating(movieInfo.getString(VOTE_AVERAGE));
                movies[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
            }

            return movies;

        }
        else {
            MainActivity.error.setVisibility(View.VISIBLE);
            return null;
        }

    }

    @Override
    protected MovieModel[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        String movieJsonString = null;
        BufferedReader reader = null;


       Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/").buildUpon()
                .appendEncodedPath(params[0])
                .appendQueryParameter("api_key", API_KEY)
                .build();

        try {
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            movieJsonString = builder.toString();

        } catch (IOException e) {
             e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return getMoviesJson(movieJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(MovieModel[] movies) {
            listener.movieCompleted(movies);
    }
}
