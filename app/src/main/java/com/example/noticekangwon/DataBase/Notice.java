package com.example.noticekangwon.DataBase;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "notice",
        foreignKeys =

            @ForeignKey(entity = Major.class,
            parentColumns = "mId",
            childColumns = "mIdFk",
            onDelete = ForeignKey.CASCADE),
        indices = @Index("mIdFk"))
public class Notice {

    public Notice(){}

    public Notice(int mIdFk ,String mTitle, String mUri, String mDate, boolean mExtension){
        this.mIdFk = mIdFk;
        this.mTitle = mTitle;
        this.mUrl = mUri;
        this.mDate = mDate;
        this.mExtension = mExtension;
    }

    public int mIdFk;
    @PrimaryKey(autoGenerate = true)
    public int nId;
    public String mTitle;
    public String mUrl;
    public String mDate;
    public boolean mExtension;
}

