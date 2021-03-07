package com.example.notice.dataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class College implements Model {

    public College(String cName){
        this.cName = cName;
    }
    @PrimaryKey(autoGenerate = true)
    public int cId;
    public final String cName;

    @NotNull
    @Override
    public String getModel() {
        return cName;
    }
}