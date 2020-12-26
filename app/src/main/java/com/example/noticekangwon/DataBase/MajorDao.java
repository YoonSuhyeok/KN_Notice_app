package com.example.noticekangwon.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MajorDao {

    @Query("SELECT * FROM Major")
    List<Major> getAll();

    @Insert
    void insert(Major major);

    @Delete
    void delete(Major major);

    @Update
    void update(Major major);
    
}
