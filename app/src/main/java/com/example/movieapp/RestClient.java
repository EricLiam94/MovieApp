package com.example.movieapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class RestClient {
    final static String BASE_URL = "http://10.0.2.2:8080/assignment1/webresources/rest.";
    final static String OMDB_URL = "http://www.omdbapi.com/?apikey=bf592e26&";
    public static String GetRestClient(String rest){
        String urlString = BASE_URL+rest;
        return GETRest(urlString);
    }
    public  static String OMDBRest(Boolean isSearch,String src ){
        String suffix = isSearch? "s="+src:"i="+src;
        String urlString = OMDB_URL+suffix;
        return GETRest(urlString);

    }


  static String postClient(String src, Object value){
        URL url ;
        HttpURLConnection conn = null;
        try {
            Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringJson=gson.toJson(value);
            Log.e("check",stringJson);
            url = new URL(BASE_URL + src);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true

            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringJson);
            out.close();
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder res = new StringBuilder();

            while(scanner.hasNext()) {
                res.append(scanner.next());
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
            return null;
    }



    public static String GETRest(String urlString){
        HttpURLConnection conn ;
        URL url;
        try {
            Log.e("url",urlString);
            url  =  new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder res = new StringBuilder();

            while(scanner.hasNextLine()) {
                res.append(scanner.nextLine());
            }
            return res.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }
}
