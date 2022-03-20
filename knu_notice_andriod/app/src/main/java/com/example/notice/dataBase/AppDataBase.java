package com.example.notice.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {College.class, Major.class, Notice.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CollegeDao collegeDao();
    public abstract MajorDao majorDao();
    public abstract NoticeDao noticeDao();
}