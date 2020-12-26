package com.example.noticekangwon.DataBase;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;

@Entity(tableName = "major",
        foreignKeys = @ForeignKey(
                entity = College.class,
                parentColumns = "cId",
                childColumns = "cIdFk",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("cIdFk")
)
public class Major {

    public Major(String mName){
        this.mName = mName;
    }
    @PrimaryKey(autoGenerate = true)
    public int mId;
    public int cIdFk;
    public String mName;

}