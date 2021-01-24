package com.example.notice.dataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "notice",
        foreignKeys =
        @ForeignKey(entity = Major.class,
                parentColumns = "mId",
                childColumns = "mIdFk",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("mIdFk"))
public class Notice {

    public Notice(int mIdFk, @NotNull String mTitle, String mUrl, String mDate) {
        this.mIdFk = mIdFk;
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mDate = mDate;
        isPin = 1;
        isBookmark = false;
    }

    public final int mIdFk;
    @NonNull
    @PrimaryKey
    public final String mTitle;
    public final String mUrl;
    public final String mDate;
    public int isPin;
    public boolean isBookmark;
}