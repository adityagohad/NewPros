package pros.app.com.pros.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.base.DateUtils;
import pros.app.com.pros.detail.presenter.DetailPresenter;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private List<PostModel> comments;
    private DetailPresenter detailPresenter;


    public CommentsAdapter(Context context, List<PostModel> comments, DetailPresenter detailPresenter) {
        this.comments = comments;
        this.detailPresenter = detailPresenter;
    }

    public void setData(List<PostModel> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        AthleteModel currentItem = comments.get(position).getAthlete();
        String dateDifference = DateUtils.getDateDifference(comments.get(position).getCreatedAt(), true);

        Picasso.get().load(currentItem.getAvatar().getThumbnailUrl()).placeholder(R.drawable.profile).into(holder.ivIcon);
        holder.tvName.setText(String.format("%s %s", currentItem.getFirstName(), currentItem.getLastName()));
        if(TextUtils.isEmpty(dateDifference)){
            holder.tvTime.setText("");
        } else {
            holder.tvTime.setText(dateDifference.toLowerCase());
        }
        holder.tvComment.setText(comments.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.tvTime)
        TextView tvTime;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            detailPresenter.onClickingComment(comments.get(getAdapterPosition()).getId(), getAdapterPosition());
        }
    }
}
