package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.movieapp.Database.Movie;
import com.example.movieapp.Models.Cinema;
import com.example.movieapp.Models.Memoir;
import com.example.movieapp.Models.Users;
import com.example.movieapp.Utility.DownloadImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AddMemoirActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner sp ;
    Calendar date;
    ArrayAdapter<Cinema> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memoir);
        init();
        new RetriveCinema().execute();



    }


    private  void init(){
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        String release = it.getStringExtra("release");
        String title = it.getStringExtra("title");
        String poster = it.getStringExtra("poster");
        String id = it.getStringExtra("id");
        Users user = bundle.getParcelable("user");
        Log.e( "==========",user.toString());
        Log.e("==========",release);
        Button bt = findViewById(R.id.date_time_picker);
        bt.setOnClickListener(this);
        TextView name_tv = findViewById(R.id.movie_name_tv);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        TextView release_tv = findViewById(R.id.movie_release_tv);
        ImageView movie_image_v = findViewById(R.id.movie_image);
        new DownloadImageTask(movie_image_v).execute(poster);
        Button submitBt = findViewById(R.id.post_cinema);
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
                try {
                    Date d  = simpleDateFormat.parse(release);
                    float rate =  ratingBar.getRating();
//                    {"cid":1,"cname":"Hoyts","location":"3145"},
                    Cinema  cinema = new Cinema(1,"Hoyts","3145");
                   Memoir memoir = new Memoir(title,d,date.getTime(),(double)rate,cinema,user);

                   new PostMemoir().execute(memoir);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        name_tv.setText(title);
        release_tv.setText(release);
        sp = findViewById(R.id.cinema_spinner);
        sp.setOnItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {
        showDateTimePicker();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       Cinema selected =  arrayAdapter.getItem(position);
       Toast.makeText(getApplicationContext(),selected.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class RetriveCinema extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String res = RestClient.GetRestClient("cinema");
                return res;
            }
            catch (Exception e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                ArrayList<Cinema> list = new ArrayList<>();
                Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
                for (int i = 0; i<jsonArray.length();i++)
                {

                   Cinema cinema =  gson.fromJson(jsonArray.getJSONObject(i).toString(),Cinema.class);
                    list.add(cinema);
                }
                arrayAdapter =
                  new ArrayAdapter<>(AddMemoirActivity.this,android.R.layout.simple_spinner_item,list);
                arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(arrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(AddMemoirActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(AddMemoirActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


 class PostMemoir extends AsyncTask<Memoir,Void,Boolean>
 {
     @Override
     protected Boolean doInBackground(Memoir... memoirs) {
         try {
             RestClient.postClient("memoir",memoirs[0]);
             return true;
         }catch (Exception e){e.printStackTrace();}

         return false;
     }

     @Override
     protected void onPostExecute(Boolean aBoolean) {
         super.onPostExecute(aBoolean);
         if (aBoolean) {
             Toast.makeText(getApplicationContext(),"Post memoir successfully",Toast.LENGTH_LONG).show();
             finish();
         }
     }
 }



}
