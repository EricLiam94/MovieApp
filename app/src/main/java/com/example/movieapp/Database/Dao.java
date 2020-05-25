package com.example.movieapp.Database;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@androidx.room.Dao
public interface Dao {
    @Insert
    void insert(Movie... m);

    @Delete
    void delete(Movie... m);

    @Update
    void update(Movie... m);

    @Query("select * from movie")
    List<Movie> getAll();

}
