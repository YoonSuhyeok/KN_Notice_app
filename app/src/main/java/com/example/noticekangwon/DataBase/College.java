package com.example.noticekangwon.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class College {

    public College(String cName){
        this.cName = cName;
    }
    @PrimaryKey(autoGenerate = true)
    public int cId;
    public String cName;

}