package pros.app.com.pros.create_question.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.create_question.view.TagsView;
import pros.app.com.pros.home.model.AthleteModel;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> implements Filterable {

    private ArrayList<AthleteModel> athleteModelList = new ArrayList<>();
    private Context context;
    private ArrayList<AthleteModel> modelFiltered = new ArrayList<>();
    private ArrayList<AthleteModel> userSelectedList = new ArrayList<>();
    private TagsView tagsView;


    public TagsAdapter(Context context, ArrayList<AthleteModel> athleteModelList,
                       ArrayList<AthleteModel> userSelectedList, TagsView tagsView) {
        this.context = context;
        this.athleteModelList.clear();
        this.athleteModelList = athleteModelList;
        this.modelFiltered.clear();
        this.modelFiltered.addAll(athleteModelList);
        this.tagsView = tagsView;
        if (userSelectedList.size() > 0) {
            this.userSelectedList = userSelectedList;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final AthleteModel currentItem = modelFiltered.get(position);

        Picasso.get().load(currentItem.getAvatar().getThumbnailUrl()).placeholder(R.drawable.profile).into(holder.ivIcon);
        holder.tvName.setText(String.format("%s %s", currentItem.getFirstName(), currentItem.getLastName()));

        if (userSelectedList.contains(currentItem)) {
            holder.addToList = !holder.addToList;
            holder.ivFollow.setVisibility(View.VISIBLE);
            holder.ivTags.setVisibility(View.GONE);
        } else {
            holder.ivFollow.setVisibility(View.GONE);
            holder.ivTags.setVisibility(View.VISIBLE);
        }

        holder.athleteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.updateViews();
                if (holder.addToList && !userSelectedList.contains(currentItem)) {
                    userSelectedList.add(currentItem);

                } else {
                    if (userSelectedList.contains(currentItem)) {
                        userSelectedList.remove(currentItem);
                    }
                }
                tagsView.updateUserSelectedList(userSelectedList);
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
                modelFiltered = (ArrayList<AthleteModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.ivFollow)
        ImageView ivFollow;
        @BindView(R.id.ivTags)
        ImageView ivTags;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.athlete_container)
        ConstraintLayout athleteContainer;

        private boolean addToList = false;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivTags.setVisibility(View.VISIBLE);
            ivFollow.setVisibility(View.GONE);
        }

        void updateViews() {
            addToList = !addToList;
            if (addToList) {
                ivFollow.setVisibility(View.VISIBLE);
                ivTags.setVisibility(View.GONE);
            } else {
                ivFollow.setVisibility(View.GONE);
                ivTags.setVisibility(View.VISIBLE);
            }
        }

    }
}
