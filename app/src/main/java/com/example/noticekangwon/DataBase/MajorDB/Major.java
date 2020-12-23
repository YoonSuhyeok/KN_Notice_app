package com.example.noticekangwon.DataBase.MajorDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Major {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String mName;

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}