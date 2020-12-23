package com.example.noticekangwon.DataBase.MajorDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {College.class, Major.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CollegeDao collegeDao();
    public abstract MajorDao majorDao();
}