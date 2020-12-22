package com.example.noticekangwon.DataBase.MajorDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CollegeDao {
    @Query("SELECT * FROM College")
    List<College> getAll();

    @Insert
    void insert(College college);

    @Delete
    void delete(College college);

    @Update
    void update(College college);
}
