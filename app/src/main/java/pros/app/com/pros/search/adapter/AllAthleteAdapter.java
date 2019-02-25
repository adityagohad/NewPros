package pros.app.com.pros.search.adapter;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.profile.activity.AthleteProfileActivity;

import static pros.app.com.pros.base.ProsConstants.IMAGE_URL;
import static pros.app.com.pros.base.ProsConstants.NAME;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class AllAthleteAdapter extends RecyclerView.Adapter<AllAthleteAdapter.FollowingViewHolder> implements Filterable {

    private List<AthleteModel> athleteModelList = new ArrayList<>();
    private Context context;
    private List<AthleteModel> modelFiltered = new ArrayList<>();

    public AllAthleteAdapter(Context context, List<AthleteModel> athleteModelList) {
        this.context = context;
        this.athleteModelList.clear();
        this.athleteModelList = athleteModelList;
        this.modelFiltered.clear();
        this.modelFiltered.addAll(athleteModelList);
    }


    @Override
    public FollowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_athete_row, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, int position) {
        AthleteModel currentItem = modelFiltered.get(position);

        Picasso.get().load(currentItem.getAvatar().getThumbnailUrl()).placeholder(R.drawable.profile).into(holder.ivIcon);
        holder.tvName.setText(String.format("%s %s", currentItem.getFirstName(), currentItem.getLastName()));

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

    public class FollowingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.athlete_row)
        RelativeLayout athleteRow;

        public FollowingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            athleteRow.setOnClickListener(this);
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.athlete_row:
                    Intent intent = new Intent(context, AthleteProfileActivity.class);
                    intent.putExtra(PROFILE_ID, athleteModelList.get(getAdapterPosition()).getId());
                    intent.putExtra(IMAGE_URL, athleteModelList.get(getAdapterPosition()).getAvatar().getMediumUrl());
                    intent.putExtra(NAME, String.format("%s %s", athleteModelList.get(getAdapterPosition()).getFirstName(), athleteModelList.get(getAdapterPosition()).getLastName()));
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
