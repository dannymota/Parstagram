package com.example.parstagram;

import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {

    private Post post;
    private TextView tvUsername;
    private TextView tvCreatedAt;
    private ImageView ivPostImage;
    private TextView tvDescription;
    private ImageView ivProfileImage;
    private TextView tvLikes;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsername = findViewById(R.id.tvUsername);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        ivPostImage = findViewById(R.id.ivPostImage);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvLikes = findViewById(R.id.tvLikes);

        tvUsername.setText(post.getUser().getUsername());
        String sourceString = "<b>" + post.getUser().getUsername() + "</b> " + post.getDescription();
        tvDescription.setText(Html.fromHtml(sourceString));
        tvCreatedAt.setText(getRelativeTimeAgo(String.valueOf(post.getCreatedAt())));
        int likeCount = queryLikes(post);
        tvLikes.setText(String.valueOf(likeCount) + (likeCount == 1 ? " Like" :" Likes"));

        ParseFile image = post.getImage();

        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostImage);
        } else {
            Glide.with(this).load(R.drawable.ic_launcher_foreground).into(ivPostImage);
        }

        if (post.getUser().getParseFile(Post.KEY_IMAGE) != null) {
            Glide.with(this).load(post.getUser().getParseFile(Post.KEY_IMAGE).getUrl()).into(ivProfileImage);
        } else {
            Glide.with(this).load(R.drawable.ic_launcher_foreground).into(ivProfileImage);
        }
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public int queryLikes(Post post) {
        ParseQuery query = post.getRelation("likes").getQuery();
        query.whereEqualTo(Post.KEY_OBJECT_ID, ParseUser.getCurrentUser().getObjectId());
        try {
            List<ParseObject> userLiked = query.find();
            return userLiked.size();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}