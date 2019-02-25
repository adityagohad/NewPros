package pros.app.com.pros.profile.activity;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.profile.adapter.ContactsAdapter;
import pros.app.com.pros.profile.model.ContactsModel;
import pros.app.com.pros.profile.presenter.ChangePasswordPresenter;
import pros.app.com.pros.profile.views.ContactsView;

public class ContactListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, ContactsView {
    @BindView(R.id.conatcts_list)
    RecyclerView rvContacts;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    List<ContactsModel> contactsModel = new ArrayList<>();
    TreeMap<String, String> contacts = new TreeMap<>();
    private String phone;
    private String name;
    private ContactsAdapter contactsAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(1, null, this);
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, null,null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            contacts.put("" + cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)),
                    "" + cursor.getString(cursor.getColumnIndex(NUMBER)));
            cursor.moveToNext();
        }

        Iterator it = contacts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            contactsModel.add(new ContactsModel(pair.getKey().toString(), pair.getValue().toString(), false));
        }

        rvContacts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        contactsAdapter = new ContactsAdapter(contactsModel, this);
        rvContacts.setAdapter(contactsAdapter);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @Override
    public void onItemClick(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @OnClick(R.id.tvSend)
    public void onClickSend() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonRequest = new JSONObject();
        try {

            if (!TextUtils.isEmpty(phone)) {
                jsonRequest.put("phone_number", phone
                        .replace("-", "")
                        .replace("+1", "")
                        .replace("(", "")
                        .replace(")", "")
                        .replace("+91", ""));
            }

            if (!TextUtils.isEmpty(name)) {
                jsonRequest.put("name", name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ChangePasswordPresenter changePasswordPresenter = new ChangePasswordPresenter(this);
        changePasswordPresenter.sendInvite(jsonRequest.toString());
    }

    @Override
    public void onSuccess() {
        openDialog("Success", "Your invite has been sent!", "Ok");
    }

    private void openDialog(String title, String message, String action) {
        progressBar.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Content", message);
        bundle.putString("Action2", action);
        bundle.putString("LAST_SCREEN", ContactListActivity.class.getName());
        CustomDialogFragment.newInstance(bundle).show(getSupportFragmentManager(), CustomDialogFragment.TAG);
    }

    @Override
    public void onFailure(int message) {
        openDialog("Pending Invite", "This Athlete has a pending invitation to join Pros", "Close");
    }
}