package com.example.movieapp.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.movieapp.Models.Users;
import com.example.movieapp.MovieViewActivity;
import com.example.movieapp.R;
import com.example.movieapp.RestClient;
import com.example.movieapp.Utility.DownloadImageTask;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Movie_SearchFragment extends Fragment implements View.OnClickListener {


    private EditText searchField;
    LinearLayout contianer;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);
       searchField = root.findViewById(R.id.search_et);
        contianer = root.findViewById(R.id.movie_container);
        Button bt = root.findViewById(R.id.search_bt);
        bt.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        String title = searchField.getText().toString();
        contianer.removeAllViews();
        new GetMovie().execute(title);
    }

    class GetMovie extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String res = RestClient.OMDBRest(true,strings[0]);

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
          Bundle bundle = getActivity().getIntent().getExtras();
          final Users u = bundle.getParcelable("user");
          Log.e("asd",u.getAddress());
            try {
                JSONObject jsonObject = new JSONObject(s);
               if ( jsonObject.getString("Response").equals("False"))
                   searchField.setError("Invalid input");
               else{
                   JSONArray jsonArray = jsonObject.getJSONArray("Search");
                   for(int i =0;i<jsonArray.length();i++)
                   {
                       final JSONObject item = jsonArray.getJSONObject(i);
                       View v  =   vi.inflate(R.layout.movierow,null);
                       TextView movie_name_tv = v.findViewById(R.id.movie_name);
                       TextView release_tv = v.findViewById(R.id.movie_release);
                       ImageView imageView = v.findViewById(R.id.movie_poster);
                       new DownloadImageTask(imageView).execute(item.getString("Poster"));
                       movie_name_tv.setText(item.getString("Title"));
                       release_tv.setText(item.getString("Year"));

                       v.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               try {
                                   Intent it = new Intent(getActivity(), MovieViewActivity.class);
                                   it.putExtra("id",item.getString("imdbID"));
                                   Bundle bundle = new Bundle();
                                   bundle.putParcelable("user",u);
                                   it.putExtras(bundle);
                                   startActivity(it);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
                       contianer.addView(v);
                   }

               }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}