package com.example.noticekangwon.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoticeDao {
    @Query("SELECT * FROM Notice")
    List<Notice> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Notice notice);

    @Delete
    void delete(Notice notice);

    @Update
    void update(Notice notice);
}

