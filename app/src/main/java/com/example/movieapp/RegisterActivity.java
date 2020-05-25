package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.movieapp.Models.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    Button pickDateBt,credentialBt;
    EditText firstNameEt,lastNameEt,addressEt,postEt;
    ProgressBar progressBar;
    RadioGroup genderRadio;
    String ustate,dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         pickDateBt = findViewById(R.id.pickdateBt);
        pickDateBt.setOnClickListener(this);
         credentialBt = findViewById(R.id.go_next_BT);
        firstNameEt = findViewById(R.id.firstname_et);
        lastNameEt = findViewById(R.id.surname_et);
        genderRadio = findViewById(R.id.gender_radio);
        addressEt = findViewById(R.id.address_et);
        postEt = findViewById(R.id.post_et);
        Spinner sp = findViewById(R.id.state_spinner);
        progressBar = findViewById(R.id.prograss_register);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.aus_states,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        credentialBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname =firstNameEt.getText().toString();
                String surname = lastNameEt.getText().toString();
                String gender = ((RadioButton)findViewById(genderRadio.getCheckedRadioButtonId())).getText().toString();
                String address = addressEt.getText().toString();
                String post = postEt.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(dob);
                    Users u = new Users(firstname,surname,gender.charAt(0),date,address,post,ustate);
                    new PostToBackEnd().execute(u);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String birthday = String.valueOf(year) + "-" +String.valueOf(month) + "-" +String.valueOf(dayOfMonth);
        pickDateBt.setText( birthday);
        dob = birthday;
        Toast.makeText(this,"Your birthday is " + birthday,Toast.LENGTH_LONG).show();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        ustate = selected;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class PostToBackEnd extends AsyncTask<Users,Void,Users>{
        @Override
        protected Users doInBackground(Users... users) {
        Users u = users[0];
        Log.e("uid",String.valueOf(u.getId()));
        String str =    RestClient.postClient("users/newUser",u);
        try {
            JSONObject obj = new JSONObject(str);
          String id =   obj.getString("uid");
          u.setId(Integer.parseInt(id));
          return u;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
        }

        @Override
        protected void onPostExecute(Users user) {
            super.onPostExecute(user);
            Intent it = new Intent(RegisterActivity.this, CredentialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user",user);
            it.putExtras(bundle);
            startActivity(it);
        }
    }

}
