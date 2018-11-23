package com.example.frank.commemorativebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.UUID;

public class AuthorInfoActivity extends AppCompatActivity {

    private ImageView img_author;
    private String wlecomUrl="http://114.116.94.16:8080/SpecialDeviceExam/static/Image/Advertise/author.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_info);
        img_author=findViewById(R.id.img_author);
        String keysig=UUID.randomUUID().toString();
        Glide.with(AuthorInfoActivity.this).load(wlecomUrl).signature(new StringSignature(keysig)).into(img_author);
    }
}
