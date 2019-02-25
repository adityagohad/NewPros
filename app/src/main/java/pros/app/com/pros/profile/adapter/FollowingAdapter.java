package pros.app.com.pros.profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.base.KeyboardAction;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.profile.activity.AthleteProfileActivity;
import pros.app.com.pros.profile.presenter.FollowingPresenter;

import static pros.app.com.pros.base.ProsConstants.IMAGE_URL;
import static pros.app.com.pros.base.ProsConstants.NAME;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> implements Filterable {
    private FollowingPresenter presenter;
    private List<AthleteModel> athleteModelList = new ArrayList<>();
    private Context context;
    private List<AthleteModel> modelFiltered = new ArrayList<>();

    public FollowingAdapter(Context context, List<AthleteModel> athleteModelList, FollowingPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        this.athleteModelList.clear();
        this.athleteModelList = athleteModelList;
        this.modelFiltered.clear();
        this.modelFiltered.addAll(athleteModelList);
    }


    @Override
    public FollowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowingViewHolder holder, int position) {
        final AthleteModel currentItem = modelFiltered.get(position);

        Picasso.get().load(currentItem.getAvatar().getThumbnailUrl()).placeholder(R.drawable.profile).into(holder.ivIcon);
        holder.tvName.setText(String.format("%s %s", currentItem.getFirstName(), currentItem.getLastName()));

        if (currentItem.isFollowedByCurrentUser()) {
            holder.ivFollow.setVisibility(View.VISIBLE);
            holder.ivUnFollow.setVisibility(View.GONE);
        } else {
            holder.ivFollow.setVisibility(View.GONE);
            holder.ivUnFollow.setVisibility(View.VISIBLE);
        }

        holder.ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardAction.hideSoftKeyboard(context, holder.ivFollow);
                currentItem.setFollowedByCurrentUser(false);
                String name = String.format("%s %s", currentItem.getFirstName(),
                        currentItem.getLastName());
                presenter.unFollowAthlete(currentItem.getId(), name);
            }
        });

        holder.ivUnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardAction.hideSoftKeyboard(context, holder.ivUnFollow);
                currentItem.setFollowedByCurrentUser(true);
                presenter.followAthlete(currentItem.getId());
            }
        });

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AthleteProfileActivity.class);
                intent.putExtra(PROFILE_ID, currentItem.getId());
                intent.putExtra(IMAGE_URL, currentItem.getAvatar().getMediumUrl());
                intent.putExtra(NAME, String.format("%s %s", currentItem.getFirstName(), currentItem.getLastName()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                if (searchString.isEmpty()) {
                    modelFiltered.clear();
                    modelFiltered.addAll(athleteModelList);
                } else {
                    modelFiltered.clear();
                    for (int i = 0; i < athleteModelList.size(); i++) {
                        String name = String.format("%s %s", athleteModelList.get(i).getFirstName().toLowerCase(), athleteModelList.get(i).getLastName().toLowerCase());
                        if (name.startsWith(searchString.toLowerCase())) {
                            modelFiltered.add(athleteModelList.get(i));
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = modelFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                modelFiltered = (List<AthleteModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.ivUnFollow)
        ImageView ivUnFollow;
        @BindView(R.id.ivFollow)
        ImageView ivFollow;
        @BindView(R.id.tvName)
        TextView tvName;

        public FollowingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}