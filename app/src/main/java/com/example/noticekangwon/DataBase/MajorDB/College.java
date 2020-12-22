package com.example.noticekangwon.DataBase.MajorDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class College {

    @PrimaryKey(autoGenerate = true)
    private int cId;
    private String cName;

    public void setId(int cId) {
        this.cId = cId;
    }

    public void setName(String cName) {
        this.cName = cName;
    }

    public int getId() {
        return cId;
    }

    public String getName() {
        return cName;
    }
}
