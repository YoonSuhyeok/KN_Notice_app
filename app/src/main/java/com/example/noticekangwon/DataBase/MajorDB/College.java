package com.example.noticekangwon.DataBase.MajorDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class College {

    @PrimaryKey(autoGenerate = true)
    private int cId;
    private String cName;

    public void setcId(int cId) {
        this.cId = cId;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getcId() {
        return cId;
    }

    public String getcName() {
        return cName;
    }
}