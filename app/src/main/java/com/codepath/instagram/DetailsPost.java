package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

public class DetailsPost extends AppCompatActivity {

    TextView mUser;
    TextView mUsername;
    ImageView mImage;
    TextView mCaption;
    TextView mTimestamp;
    Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_post);
        mCaption = findViewById(R.id.tvCaption);
        mUser = findViewById(R.id.tvUser);
        mUsername = findViewById(R.id.tvUser2);
        mImage = findViewById(R.id.ivPostImage);
        mTimestamp = findViewById(R.id.tvTimestamp);

        mPost = (Post) getIntent().getSerializableExtra("post");

        mCaption.setText(mPost.getDescription());
        mUser.setText(mPost.getUser().getUsername());
        mUsername.setText(mPost.getUser().getUsername());
        mTimestamp.setText(Post.calculateTimeAgo(mPost.getCreatedAt()));
        if (mPost.getImage() != null) {
            Glide.with(this).load(mPost.getImage().getUrl()).into(mImage);
        }
    }
}