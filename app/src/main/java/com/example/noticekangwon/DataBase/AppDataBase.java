package com.example.noticekangwon.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {College.class, Major.class}, version = 2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CollegeDao collegeDao();
    public abstract MajorDao majorDao();
//    public abstract NoticeDao noticeDao();

}