package com.example.movieapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1,exportSchema=false)
public   abstract class MovieDB extends RoomDatabase {
    public  abstract Dao dao();
    public static volatile MovieDB instance;

    public static synchronized MovieDB getInstance(Context context) {
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MovieDB.class,"Movie")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}