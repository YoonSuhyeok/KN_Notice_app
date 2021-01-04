package com.example.noticekangwon.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
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

    @Query("SELECT * FROM Major WHERE mName = :name")
    int getFK(String name);

    @Query("SELECT cName FROM College")
    LiveData<String> getList();
}