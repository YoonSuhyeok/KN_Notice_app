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
    @Query("SELECT * FROM Notice ORDER BY isPin, mDate DESC")
    List<Notice> getAll();

    @Query("SELECT * FROM Notice WHERE mIdFk IN (:ids) ORDER BY isPin, mDate DESC")
    List<Notice> getFil(List<Integer> ids);

    @Query("SELECT * FROM Notice WHERE mTitle LIKE :title")
    Notice getNotice(String title);

    @Query("SELECT * FROM Notice WHERE isBookmark = :bookOn")
    List<Notice> getBookmark(boolean bookOn);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Notice notice);

    @Delete
    void delete(Notice notice);

    @Update
    void update(Notice notice);
}

