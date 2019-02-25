package pros.app.com.pros.home.presenter;

import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.home.view.HomeView;

public class HomePresenter implements HttpServiceView {


    private HomeView view;
    private HomeMainModel homePostModel;
    private boolean postsAPIHit = false, questionsAPIHit = false;
    private ArrayList<PostModel> postsList;
    private boolean pullToRefresh;

    public HomePresenter(HomeView view) {
        this.view = view;
        homePostModel = new HomeMainModel();
    }

    public void getPostData(boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
        new HttpServiceUtil(
                this,
                ApiEndPoints.post_content.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.post_content.getTag()
        ).execute();

        new HttpServiceUtil(this,
                ApiEndPoints.questions.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.questions.getTag()
        ).execute();
    }


    @Override
    public void response(String response, int tag) {

        if (tag == ApiEndPoints.post_content.getTag()) {
            postsAPIHit = true;
            try {
                HomeMainModel postsData = JsonUtils.from(response, HomeMainModel.class);
                homePostModel.setPosts(postsData.getPosts());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tag == ApiEndPoints.questions.getTag()) {
            questionsAPIHit = true;
            try {
                HomeMainModel questionsData = JsonUtils.from(response, HomeMainModel.class);
                homePostModel.setQuestions(questionsData.getQuestions());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (postsAPIHit && questionsAPIHit) {
            postsList = new ArrayList<>();
            if (homePostModel.getPosts() != null) postsList.addAll(homePostModel.getPosts());
            if (homePostModel.getQuestions() != null)
                postsList.addAll(homePostModel.getQuestions());
            Log.d("Merged List:", "" + postsList);
            Collections.sort(postsList, new Comparator<PostModel>() {
                @Override
                public int compare(PostModel o1, PostModel o2) {
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    Date date1 = new Date();
                    Date date2 = new Date();
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        date1 = format.parse(o1.getCreatedAt());
                        date2 = format.parse(o2.getCreatedAt());
                        Log.e("Dates", "" + date1 + date2);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date2.compareTo(date1);
                }
            });
            Log.d("Sorted List:", "" + postsList);

            if (pullToRefresh) {
                view.updateHomeScreen(postsList);
            } else {
                view.bindData(postsList);
            }
        }
    }

    @Override
    public void onError(int tag) {

    }
}
