package pros.app.com.pros.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseDialogFragment extends DialogFragment {

    protected AppCompatActivity mContext;

    protected abstract int getResourceId();

    protected abstract String getClassTag();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mContext = (AppCompatActivity) context;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getResourceId();
        if (resId == 0)
            return super.onCreateView(inflater, container, savedInstanceState);

        final View rootView = inflater.inflate(resId, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
