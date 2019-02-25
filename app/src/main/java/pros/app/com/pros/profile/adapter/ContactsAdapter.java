package pros.app.com.pros.profile.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.profile.model.ContactsModel;
import pros.app.com.pros.profile.views.ContactsView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private final List<ContactsModel> contactsModel;
    private final ContactsView contactsView;
    private int mSelectedPosition = -1;

    public ContactsAdapter(List<ContactsModel> contactsModel, ContactsView contactsView) {
        this.contactsModel = contactsModel;
        this.contactsView = contactsView;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position) {

        holder.tvName.setText(contactsModel.get(position).getName());

        if (position == mSelectedPosition) {
            holder.tvName.setBackgroundColor(ContextCompat.getColor(holder.tvName.getContext(), R.color.bg_light_gray));
            holder.tvName.setTextColor(ContextCompat.getColor(holder.tvName.getContext(), R.color.white));
        } else {
            holder.tvName.setBackgroundColor(ContextCompat.getColor(holder.tvName.getContext(), R.color.white));
            holder.tvName.setTextColor(ContextCompat.getColor(holder.tvName.getContext(), R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return contactsModel.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvName)
        TextView tvName;

        public ContactsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            if (mSelectedPosition != position) {
                int positionOld = mSelectedPosition;
                mSelectedPosition = position;
                if (positionOld != -1) {
                    notifyItemChanged(positionOld);
                }
                notifyItemChanged(mSelectedPosition);

                contactsView.onItemClick(contactsModel.get(getAdapterPosition()).getName(),
                        contactsModel.get(getAdapterPosition()).getPhoneNumber());
                contactsModel.get(getAdapterPosition()).setSelected(true);
            }
        }
    }
}
