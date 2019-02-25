package pros.app.com.pros.detail.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pros.app.com.pros.R;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.detail.adapter.ScreenSlidePagerAdapter;
import pros.app.com.pros.detail.fragment.DetailFragment;
import pros.app.com.pros.home.model.PostModel;

public class DetailActivity extends FragmentActivity implements DetailFragment.OnFragmentInteractionListener {


    @BindView(R.id.pager)
    ViewPager detailViewPager;

    ScreenSlidePagerAdapter detailPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent i = getIntent();
        ArrayList<PostModel> postArrayList = i.getParcelableArrayListExtra("postArray");
        String receivedContentType = "";
        if(i.hasExtra("contentType")) {
           receivedContentType = i.getStringExtra("contentType");
        }
        int currentPosition = i.getIntExtra("selectedPosition", 0);
        LogUtils.LOGE("Detail:", "" + currentPosition);
        detailPageAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), postArrayList, receivedContentType);
        detailViewPager.setAdapter(detailPageAdapter);
        detailViewPager.setCurrentItem(currentPosition);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
