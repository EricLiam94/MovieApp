package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Models.Credentials;
import com.example.movieapp.Models.Users;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class CredentialActivity extends AppCompatActivity implements View.OnClickListener {
    TextView usernameET,passwordET;
    Button bt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);
        bt = findViewById(R.id.cre_bt);
        usernameET = findViewById(R.id.cre_username);
        passwordET = findViewById(R.id.cre_password);

        bt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        Users u = bundle.getParcelable("user");
        Credentials cre = new Credentials(username,password,u);
        new PostToBackend().execute(cre);

    }


    class PostToBackend extends AsyncTask<Credentials,Integer,Credentials>{
        @Override
        protected Credentials doInBackground(Credentials... credentials) {
            Credentials cre = credentials[0];

            String password = cre.getPassword();
            String hashedPSW = Hashing.sha256().hashString(password,StandardCharsets.UTF_8).toString();
            cre.setPassword(hashedPSW);
            String res = RestClient.postClient("credentials/newCredential",credentials[0]);
            if(res==null)
                    return null;
            cre.setPassword(password);
            return cre;
        }


        @Override
        protected void onPostExecute(Credentials credentials) {
            super.onPostExecute(credentials);
            if(credentials == null){
                Toast.makeText(CredentialActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }else {
               Intent it = new Intent(CredentialActivity.this,MainActivity.class);
               Bundle bundle = new Bundle();
               bundle.putParcelable("cre",credentials);
               it.putExtras(bundle);
               startActivity(it);
            }
        }
    }
}
