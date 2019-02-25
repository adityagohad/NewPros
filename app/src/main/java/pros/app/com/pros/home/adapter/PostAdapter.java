package pros.app.com.pros.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideoImage;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.DateUtils;
import pros.app.com.pros.detail.activity.DetailActivity;
import pros.app.com.pros.detail.adapter.ReactionAthlete;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;

public class PostAdapter extends AAH_VideosAdapter {

    private ArrayList<PostModel> postsArrayList;
    private Context context;
    private String contentType;

    public PostAdapter(ArrayList<PostModel> postsArrayList, Context context, String contentType) {
        this.postsArrayList = postsArrayList;
        this.context = context;
        this.contentType = contentType;
    }

    @NonNull
    @Override
    public AAH_CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_posts, parent, false);

        return new PostsViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull AAH_CustomViewHolder holder, int position) {
        //Main logic of updating UI
        PostModel postModel = postsArrayList.get(position);
        String contentType = postModel.getContentType();
        String dateDifference = DateUtils.getDateDifference(postModel.getCreatedAt(), false);

        holder.setIsRecyclable(false);

        if (postModel.getQuestioner() != null ||
                (contentType != null &&
                        (contentType.equalsIgnoreCase("image") || contentType.equalsIgnoreCase("video")))) {
            String athleteFullName = "";
            String thumbnailUrl = "";
            String athleteThumbnailUrl = "";

            ((PostsViewHolder) holder).postLike.setText("" + postModel.getLikes().getCount());
            List<PostModel> reactionsList = postModel.getReactions();

            if (reactionsList.size() > 0) {
                ((PostsViewHolder) holder).separator.setVisibility(View.VISIBLE);
                ((PostsViewHolder) holder).athleteRecyclerview.setVisibility(View.VISIBLE);

                ArrayList<AthleteModel> athleteModels = new ArrayList<>();

                for (int i = 0; i < reactionsList.size(); i++) {
                    athleteModels.add(reactionsList.get(i).getAthlete());
                }

                ((PostsViewHolder) holder).athleteRecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                ReactionAthlete reactionAthleteAdapter = new ReactionAthlete(athleteModels, null, null);
                ((PostsViewHolder) holder).athleteRecyclerview.setAdapter(reactionAthleteAdapter);
            } else {
                ((PostsViewHolder) holder).separator.setVisibility(View.GONE);
                ((PostsViewHolder) holder).athleteRecyclerview.setVisibility(View.GONE);
            }


            if (postModel.getQuestioner() != null) {
                athleteFullName = postModel.getQuestioner().getName();
                thumbnailUrl = postModel.getQuestioner().getAvatar().getThumbnailUrl();
                athleteThumbnailUrl = postModel.getQuestioner().getAvatar().getThumbnailUrl();
                ((PostsViewHolder) holder).questionContainer.setVisibility(View.VISIBLE);
                ((PostsViewHolder) holder).questionContainer.setVisibility(View.VISIBLE);
                ((PostsViewHolder) holder).questionText.setText(postModel.getText());
                ((PostsViewHolder) holder).athleteName.setTextColor(Color.parseColor("#50e3c2"));

                holder.setImageUrl(thumbnailUrl);
                Picasso.get().load(holder.getImageUrl()).into(holder.getAAH_ImageView());
                holder.getAAH_ImageView().setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else {
                thumbnailUrl = postModel.getUrls().getMediumUrl();
                athleteThumbnailUrl = postModel.getAthlete().getAvatar().getThumbnailUrl();
                athleteFullName = postModel.getAthlete().getFirstName() + " " + postModel.getAthlete().getLastName();
                ((PostsViewHolder) holder).questionContainer.setVisibility(View.GONE);
                ((PostsViewHolder) holder).questionContainer.setVisibility(View.GONE);
                ((PostsViewHolder) holder).athleteName.setTextColor(Color.parseColor("#ffffff"));

                holder.setVideoUrl(postModel.getUrls().getIntroUrl());
                // holder.setVideoUrl(ProsApplication.getProxy().getProxyUrl(postModel.getUrls().getIntroUrl()+"")); // url should not be null
                holder.setImageUrl(thumbnailUrl);
                Picasso.get().load(holder.getImageUrl()).into(holder.getAAH_ImageView());
            }

            Picasso.get().load(athleteThumbnailUrl).into(((PostsViewHolder) holder).athleteThumb);
            ((PostsViewHolder) holder).athleteName.setText(athleteFullName);
            ((PostsViewHolder) holder).createdAt.setText(dateDifference);

        }

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public class PostsViewHolder extends AAH_CustomViewHolder implements View.OnClickListener {

        //Bind the view
        @BindView(R.id.post_image)
        AAH_VideoImage postImage;

        @BindView(R.id.created_at)
        TextView createdAt;

        @BindView(R.id.post_like)
        TextView postLike;

        @BindView(R.id.athlete_name)
        TextView athleteName;

        @BindView(R.id.question_container_background)
        View questionContainerBackground;

        @BindView(R.id.question_container)
        RelativeLayout questionContainer;

        @BindView(R.id.question_title)
        TextView questionTitle;

        @BindView(R.id.question_text)
        TextView questionText;

        @BindView(R.id.athlete_thumb)
        CircleImageView athleteThumb;

        @BindView(R.id.post_container)
        FrameLayout postContainer;

        @BindView(R.id.athlete_list)
        RecyclerView athleteRecyclerview;
        @BindView(R.id.separator)
        View separator;

        public PostsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            postContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.post_container:
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("postArray", postsArrayList);
                    intent.putExtra("selectedPosition", this.getLayoutPosition());
                    intent.putExtra("contentType", contentType);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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

    // Clean all elements of the recycler
    public void clear() {
        postsArrayList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<PostModel> postsArrayListt) {
        postsArrayList.addAll(postsArrayListt);
        notifyDataSetChanged();
    }


}
