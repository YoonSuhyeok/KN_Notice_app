package com.example.noticekangwon.DataBase.NotcieDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Notice.class}, version = 1)
public abstract class NoticeDataBase extends RoomDatabase {
    public abstract NoticeDao noticeDao();
}
