//package com.example.noticekangwon.DataBase;
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.Index;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "notice",
//        foreignKeys =
//        @ForeignKey(entity = Major.class,
//        parentColumns = "mId",
//        childColumns = "mIdFk",
//        onDelete = ForeignKey.CASCADE),
//        indices = @Index("mIdFk"))
//public class Notice {
//
//    public Notice(int IdFk ,String Title, String Uri, String Date, boolean Extension){
//        mIdFk = IdFk;
//        mTitle = Title;
//        mUrl = Uri;
//        mDate = Date;
//        mExtension = Extension;
//    }
//
//    public int mIdFk;
//    @PrimaryKey(autoGenerate = true)
//    public int nId;
//    public String mTitle;
//    public String mUrl;
//    public String mDate;
//    public boolean mExtension;
//
//}