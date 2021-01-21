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
public interface MajorDao {

    @Query("SELECT * FROM Major")
    List<Major> getAll();

    @Query("SELECT mId FROM Major WHERE mName LIKE :name")
    int getMId(String name);

    @Insert
    void insert(Major major);

    @Delete
    void delete(Major major);

    @Update
    void update(Major major);

    @Query("SELECT mName FROM Major")
    LiveData<String> getList();
}
