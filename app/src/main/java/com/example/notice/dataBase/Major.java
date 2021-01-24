package com.example.notice.dataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Major {

    public Major(int cIdFk, String mName) {
        this.cIdFk = cIdFk;
        this.mName = mName;
    }

    @PrimaryKey(autoGenerate = true)
    public int mId;
    public final int cIdFk;
    public final String mName;
}