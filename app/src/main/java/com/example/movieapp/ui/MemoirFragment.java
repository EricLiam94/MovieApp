package com.example.movieapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.movieapp.Models.Cinema;
import com.example.movieapp.Models.Memoir;
import com.example.movieapp.R;
import com.example.movieapp.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MemoirFragment extends Fragment {

LinearLayout linear_container ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_memoir, container, false);
        linear_container = root.findViewById(R.id.memoir_container);
        new RetriveMemoir().execute();
        return root;
    }

    class RetriveMemoir extends AsyncTask<Integer,Void,String>{
        @Override
        protected String doInBackground(Integer... integers) {
         try {
             String res =   RestClient.GetRestClient("memoir");
           return res;
         }catch (Exception e){}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0;i<jsonArray.length();i++)
                {
                    Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
                    Memoir memoir = gson.fromJson(jsonArray.getJSONObject(i).toString(),Memoir.class);

                    View v = vi.inflate(R.layout.simple_memoir_row,null);
                    TextView nameView = v.findViewById(R.id.movie_name_tv);
//                    nameView.setText(jsonArray.getJSONObject(i).toString());
                    Log.e("===============",jsonArray.getJSONObject(i).toString());
//                    TextView releaseView = v.findViewById(R.id.movie_release_tv);
//                    RatingBar ratingBar = v.findViewById(R.id.rating_bar);
//                    nameView.setText(memoir.getmName());
//                    releaseView.setText(memoir.getmRelease().toString());
//                    ratingBar.setRating(memoir.getRate().floatValue());
                    linear_container.addView(v);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}