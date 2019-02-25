package pros.app.com.pros.detail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import pros.app.com.pros.detail.fragment.DetailFragment;
import pros.app.com.pros.home.model.PostModel;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<PostModel> postModelArrayList;
    private String contentType = "";

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<PostModel> postModelArrayList, String contentType) {
        super(fm);
        this.postModelArrayList = postModelArrayList;
        this.contentType = contentType;
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(postModelArrayList.get(position), contentType);
    }

    @Override
    public int getCount() {
        return postModelArrayList.size();
    }
}
