package com.example.noticekangwon.DataBase;

import androidx.annotation.NonNull;
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

    public Notice(int mIdFk, String mTitle, String mUrl, String mDate) {
        this.mIdFk = mIdFk;
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mDate = mDate;
        isPin = 1;
        isBookmark = false;
    }

    public int mIdFk;
    @NonNull
    @PrimaryKey
    public String mTitle;
    public String mUrl;
    public String mDate;
    public int isPin;
    public boolean isBookmark;
}