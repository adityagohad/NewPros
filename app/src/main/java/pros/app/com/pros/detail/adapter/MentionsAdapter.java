package pros.app.com.pros.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.home.model.AthleteModel;

public class MentionsAdapter extends RecyclerView.Adapter<MentionsAdapter.ViewHolder> {


    private List<AthleteModel> athleteModelArrayList;


    public MentionsAdapter(List<AthleteModel> athleteModels) {
        this.athleteModelArrayList = athleteModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mention, parent, false);

        return new MentionsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AthleteModel athleteModel = athleteModelArrayList.get(position);

        String name = athleteModel.getFirstName() + " " + athleteModel.getLastName();
        holder.prosName.setText(name);

    }

    @Override
    public int getItemCount() {
        return athleteModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.athlete_name)
        TextView prosName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
