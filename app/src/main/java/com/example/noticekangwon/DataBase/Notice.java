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
    public int mIdFk;
    @PrimaryKey(autoGenerate = true)
    public int nId;
    public String contents;
}
