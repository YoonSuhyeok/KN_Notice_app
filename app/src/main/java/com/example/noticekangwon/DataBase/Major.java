package com.example.noticekangwon.DataBase;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity
public class Major {

    public Major(int cIdFk, String mName) {
        this.cIdFk = cIdFk;
        this.mName = mName;
    }

    @PrimaryKey(autoGenerate = true)
    public int mId;
    public int cIdFk;
    public String mName;
}