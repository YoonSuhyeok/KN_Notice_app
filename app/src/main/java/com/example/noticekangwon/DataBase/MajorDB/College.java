package com.example.noticekangwon.DataBase.MajorDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class College {

    public College(String cName){
        this.cName = cName;
    }
    @PrimaryKey(autoGenerate = true)
    protected int cId;
    protected String cName;

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