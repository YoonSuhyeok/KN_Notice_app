package com.example.noticekangwon.DataBase.NotcieDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notice {
    @PrimaryKey(autoGenerate = true)
    private int nId;
    private String title;
}
