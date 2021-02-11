package com.example.notice.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notice.R;

import java.util.Objects;

public class DevelopInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.develop_activity);

        Toolbar toolbar = findViewById(R.id.devToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void toSong(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/song127")));
    }

    public void toYoon(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/YoonSuhyeok")));
    }

    public void toJell(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://lottiefiles.com/45422-jelly-box")));
    }
}
