package com.example.noticekangwon.DataBase.NotcieDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notice {
    @PrimaryKey(autoGenerate = true)
    protected int nId;
    protected String title;
}
