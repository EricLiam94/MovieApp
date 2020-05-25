package com.example.movieapp.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.movieapp.Models.Users;
import com.example.movieapp.R;
import com.example.movieapp.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class HomeFragment extends Fragment {
    private TextView welcomeText;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeText = root.findViewById(R.id.text_home);
        linearLayout = root.findViewById(R.id.best_container);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        Users u = bundle.getParcelable("user");
        welcomeText.setText("Welcome "+u.getUname());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(date);
        TextView curDate_TV = root.findViewById(R.id.cur_date_tv);
        curDate_TV.setText(strDate);
       new GetMoives().execute(u.getId());
      String s =  ((Users)getActivity().getIntent().getExtras().getParcelable("user")).getAddress();
      Log.e("==========",s);
      return root;
    }


    class GetMoives extends AsyncTask<Integer,Void,String>{
        @Override
        protected String doInBackground(Integer... integers) {
           String res =  RestClient.GetRestClient("memoir/findRecentGoodMovie/"+integers[0]);

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                welcomeText.setText("error");
            }
            else{
                try {
                    Log.e("Value",s);
                    JSONArray jsonArray = new JSONArray(s);

                    LayoutInflater vi = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    for(int i = 0;i<jsonArray.length();i++)
                    {
                        final View v = vi.inflate(R.layout.simple_movie_card, null);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TextView name_tv = v.findViewById(R.id.movie_name_tv);
                        TextView date_tv = v.findViewById(R.id.movie_date_tv);
                        TextView rate_tv = v.findViewById(R.id.movie_score_tv);
                        name_tv.setText(jsonObject.getString("name"));
                        date_tv.setText(jsonObject.getString("ReleaseDate"));
                        rate_tv.setText(jsonObject.getString("Rate"));
                        linearLayout.addView(v);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}