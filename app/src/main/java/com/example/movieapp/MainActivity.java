package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Models.Credentials;
import com.example.movieapp.Models.Users;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
     EditText usernameEV ;
    EditText passwordEV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView registerBt = findViewById(R.id.register_bt);
        Button loginBt = findViewById(R.id.login_bt);
        usernameEV = findViewById(R.id.username);
        passwordEV = findViewById(R.id.password);
        Intent it = getIntent();
        if (it.getExtras()!=null)
        {
            Credentials cre = it.getExtras().getParcelable("cre");
            usernameEV.setText(cre.getUsername());
            passwordEV.setText(cre.getPassword());
        }

        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(it);
            }
        });
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (validate()){
                    String username =   usernameEV.getText().toString();
                    String password =   passwordEV.getText().toString();
                    new requestMethod().execute(username,password);
                }
            }
        });


    }

    private boolean validate(){
        if (usernameEV.length()==0) {usernameEV.setError("Invalid Input") ;      return false;}

        if (passwordEV.length()==0) {passwordEV.setError("Invalid Input"); return false;}
        return true;

    }


    private class requestMethod extends AsyncTask<String,Void, Users>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Users doInBackground(String... args) {
            try {
                String hashed = Hashing.sha256()
                        .hashString(args[1], StandardCharsets.UTF_8)
                        .toString();
                String a  = RestClient.GetRestClient("credentials/findByUserNameAndPassword/"+args[0]+"/"+hashed);
                Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
                Users u = gson.fromJson(a,Users.class);
                if (a!=null)
                    return u;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Users u) {
            super.onPostExecute(u);
            if(u==null) {
                Toast.makeText(getApplicationContext(),"Password or Username is incorrect", LENGTH_LONG).show();
            }else {
                Intent it = new Intent(MainActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user",u);
                it.putExtras(bundle);
                startActivity(it);
            }
        }
    }
}
