package pros.app.com.pros.search.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideoImage;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.detail.activity.DetailActivity;
import pros.app.com.pros.home.model.PostModel;

public class TopPostsAdapter extends AAH_VideosAdapter {

    private ArrayList<PostModel> postsArrayList;

    public TopPostsAdapter(ArrayList<PostModel> postsArrayList) {
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public AAH_CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_posts, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AAH_CustomViewHolder holder, int position) {
        //Main logic of updating UI

        PostModel postModel = postsArrayList.get(position);
        String contentType = postModel.getContentType();

        if (contentType != null &&
                (contentType.equalsIgnoreCase("image") || contentType.equalsIgnoreCase("video"))) ;
        {

            String thumbnailUrl = postModel.getUrls().getThumbnailUrl();
            String athleteThumbnailUrl = postModel.getAthlete().getAvatar().getThumbnailUrl();
            String athleteFullName = postModel.getAthlete().getFirstName() + " " + postModel.getAthlete().getLastName();
            int reactionsCount = postModel.getReactions().size();

            ((ViewHolder) holder).postLikeCount.setText("" + postModel.getLikes().getCount());
            ((ViewHolder) holder).postReactionCount.setText("" + reactionsCount);
            Picasso.get().load(athleteThumbnailUrl).into(((ViewHolder) holder).athleteThumb);
            ((ViewHolder) holder).athleteName.setText(athleteFullName);

            holder.setVideoUrl(postModel.getUrls().getIntroUrl());
            holder.setImageUrl(thumbnailUrl);
            Picasso.get().load(holder.getImageUrl()).into(holder.getAAH_ImageView());
        }

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public class ViewHolder extends AAH_CustomViewHolder implements View.OnClickListener {

        //Bind the view
        @BindView(R.id.post_image)
        AAH_VideoImage postImage;

        @BindView(R.id.post_reaction_count)
        TextView postReactionCount;

        @BindView(R.id.post_likes_count)
        TextView postLikeCount;

        @BindView(R.id.athlete_name)
        TextView athleteName;

        @BindView(R.id.athlete_thumb)
        CircleImageView athleteThumb;

        @BindView(R.id.container)
        FrameLayout container;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container:
                    Intent intent = new Intent(container.getContext(), DetailActivity.class);
                    intent.putExtra("postArray", postsArrayList);
                    intent.putExtra("selectedPosition", this.getLayoutPosition());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    container.getContext().startActivity(intent);
                    break;
            }
        }

        @Override
        public void videoStarted() {
            super.videoStarted();
            muteVideo();
        }

        @Override
        public void pauseVideo() {
            super.pauseVideo();
            muteVideo();
        }
    }

}
