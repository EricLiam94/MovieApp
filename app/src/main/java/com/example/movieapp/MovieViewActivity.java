package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Database.Movie;
import com.example.movieapp.Database.MovieDB;
import com.example.movieapp.Models.Users;
import com.example.movieapp.Utility.DownloadImageTask;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MovieViewActivity extends AppCompatActivity {
    TextView name_tv,release_tv,genre_tv,cast_tv,country_tv,director_tv,plot_tv;
    ImageView imageView;
    Movie movie;
    Users u;
    String poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        init();
        Intent it = getIntent();
        String s =it.getStringExtra("id");
        Bundle bundle = it.getExtras();
         u = bundle.getParcelable("user");
        movie = new Movie(s);
        new RetriveData().execute(s);

    }

    private void init(){
        imageView = findViewById(R.id.movie_image);
        name_tv= findViewById(R.id.movie_name_tv);
        release_tv = findViewById(R.id.movie_release_tv);
        genre_tv = findViewById(R.id.movie_genere_tv);
        country_tv = findViewById(R.id.country_tv);
        director_tv = findViewById(R.id.movie_director_tv);
        plot_tv = findViewById(R.id.movie_plot_tv);
        Button watchlistBT = findViewById(R.id.add_watchlist_bt);
        Button memoirBT = findViewById(R.id.add_memoir_bt);
        memoirBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MovieViewActivity.this,AddMemoirActivity.class);

                String release = release_tv.getText().toString();
                String title = name_tv.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putParcelable("user",u);
                it.putExtras(bundle);
                it.putExtra("release",release);
                it.putExtra("id",movie.getId());
                it.putExtra("title",title);
                it.putExtra("poster",poster);
                startActivity(it);
            }
        });
        watchlistBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new InsertMovie().execute(movie);
            }
        });
    }

    class RetriveData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            try {
                String res = RestClient.OMDBRest(false,id);
                return res;
            }catch (Exception e){e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject item = new JSONObject(s);
                String title = item.getString("Title");
                String released = item.getString("Released");
                String genre = item.getString("Genre");
                String director = item.getString("Director");
                String plot = item.getString("Plot");
                poster = item.getString("Poster");
                String country = item.getString("Country");
                String score = item.getString("Metascore");
                new DownloadImageTask(imageView).execute(poster);
                movie.setRelease(released);
                movie.setName(title);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = formatter.format(new Date());
                movie.setAddTime(formattedDate);
                name_tv.setText(title);
                release_tv.setText(released);
                genre_tv.setText(genre);
                director_tv.setText(director);
                plot_tv.setText(plot);
                country_tv.setText(country);
                RatingBar ratingBar = findViewById(R.id.movie_rate);
                ratingBar.setRating(Float.parseFloat(score)/20);
            }catch (Exception e){}
        }
    }

    class InsertMovie extends AsyncTask<Movie,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Movie... movies) {
           try {
               MovieDB.getInstance(getApplicationContext()).dao().insert(movie);
               return true;
           }catch (Exception e){e.printStackTrace();}


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            String res = aBoolean? "Insert Successfully" : "Failed to insert";
            Toast.makeText(getBaseContext(),res,Toast.LENGTH_LONG).show();

        }
    }




}
