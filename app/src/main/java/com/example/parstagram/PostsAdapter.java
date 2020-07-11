package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.fragments.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;
    private FragmentManager fragmentManager;
    private Boolean isLiked;

    public PostsAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivProfileImage;
        private ImageView ivLike;
        private TextView tvCreatedAt;
        private Button btnPost;
        private EditText etComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvCreatedAt = itemView.findViewById(R.id.tvCreateAt);
            btnPost = itemView.findViewById(R.id.btnPost);
            etComment = itemView.findViewById(R.id.etComment);
            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            tvDescription.setEllipsize(TextUtils.TruncateAt.END);
            String sourceString = "<b>" + post.getUser().getUsername() + "</b> " + post.getDescription();
            tvDescription.setText(Html.fromHtml(sourceString));
            tvUsername.setText(post.getUser().getUsername());
            if (post.getUser().getParseFile(Post.KEY_IMAGE) !=  null) {
                Glide.with(context).load(post.getUser().getParseFile(Post.KEY_IMAGE).getUrl()).into(ivProfileImage);
            } else {
                Glide.with(context).load(R.drawable.ic_launcher_foreground).into(ivProfileImage);
            }

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            } else {
                Glide.with(context).load(R.drawable.ic_launcher_foreground).into(ivImage);
            }

            tvCreatedAt.setText(PostDetailsActivity.getRelativeTimeAgo(String.valueOf(post.getCreatedAt())));

            isLiked = queryLike(post);

            if (isLiked) {
                ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ufi_heart_active));
            } else {
                ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ufi_heart));
            }

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseRelation<ParseObject> relation = post.getRelation("likes");
                    if (isLiked && post.getUser() != ParseUser.getCurrentUser()) {
                        relation.remove(ParseUser.getCurrentUser());
                        post.saveInBackground();
                        ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ufi_heart));
                        isLiked = false;
                    } else if (!isLiked && post.getUser() != ParseUser.getCurrentUser()) {
                        relation.add(ParseUser.getCurrentUser());
                        post.saveInBackground();
                        ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ufi_heart_active));
                        isLiked = true;
                    } else {
                        Log.d(TAG, "You can't like your own image.");
                    }
                }
            });

            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String body = etComment.getText().toString();
                    if (body.isEmpty()) {
                        Toast.makeText(context, "Comment can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject comment = new JSONObject();
                    try {
                        comment.put("username", ParseUser.getCurrentUser().getUsername());
                        comment.put("comment", body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    post.add("comments", comment.toString());
                    post.saveInBackground();
                    etComment.setText("");
                    Toast.makeText(context, "Comment sent", Toast.LENGTH_SHORT).show();
                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.beginTransaction().replace(R.id.flContainer, new ProfileFragment(post.getUser())).addToBackStack(null).commit();
//                    bottomNavigationView.setSelectedItemId(R.id.action_profile);
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.beginTransaction().replace(R.id.flContainer, new ProfileFragment(post.getUser())).addToBackStack(null).commit();
//                    bottomNavigationView.setSelectedItemId(R.id.action_profile);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }

        public boolean queryLike(Post post) {
            ParseQuery query = post.getRelation("likes").getQuery();
            query.whereEqualTo(Post.KEY_OBJECT_ID, ParseUser.getCurrentUser().getObjectId());
            try {
                List<ParseObject> userLiked = query.find();
                if (userLiked.size() != 0) {
                    return true;
                }
                return false;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
