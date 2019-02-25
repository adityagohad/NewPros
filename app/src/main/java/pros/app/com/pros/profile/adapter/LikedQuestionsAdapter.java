package pros.app.com.pros.profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.detail.activity.DetailActivity;
import pros.app.com.pros.home.model.PostModel;

public class LikedQuestionsAdapter extends RecyclerView.Adapter<LikedQuestionsAdapter.ViewHolder> {

    ArrayList<PostModel> likedQuestionsList;
    private Context context;

    public LikedQuestionsAdapter(Context context, ArrayList<PostModel> likedQuestionsList) {
        this.likedQuestionsList = likedQuestionsList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel postModel = likedQuestionsList.get(position);

        String athleteName = postModel.getQuestioner().getName();
        String athleteThumbUrl = postModel.getQuestioner().getAvatar().getThumbnailUrl();
        String question = postModel.getText();
        String likesCount = String.format("%d Likes", postModel.getLikes().getCount());
        String answerCount = String.format("%d Answers", postModel.getReactions().size());


        holder.athleteName.setText(athleteName);
        holder.questionDescription.setText(question);
        holder.questionLikesCount.setText(likesCount);
        holder.questionAnswerCount.setText(answerCount);

        if (athleteThumbUrl != null) {
            Picasso.get().load(athleteThumbUrl).placeholder(R.drawable.profile).into(holder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return likedQuestionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivIcon)
        CircleImageView profilePic;

        @BindView(R.id.created_at)
        TextView createdAt;

        @BindView(R.id.athlete_name)
        TextView athleteName;

        @BindView(R.id.question_description)
        TextView questionDescription;

        @BindView(R.id.question_likes_count)
        TextView questionLikesCount;

        @BindView(R.id.question_answer_count)
        TextView questionAnswerCount;

        @BindView(R.id.question_container)
        RelativeLayout questionContainer;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            questionContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.question_container:
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("postArray", likedQuestionsList);
                    intent.putExtra("selectedPosition", this.getLayoutPosition());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
