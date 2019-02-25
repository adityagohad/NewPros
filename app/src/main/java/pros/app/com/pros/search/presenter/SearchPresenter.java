package pros.app.com.pros.search.presenter;

import java.io.IOException;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.profile.model.FollowingModel;
import pros.app.com.pros.search.model.AthletesMainModel;
import pros.app.com.pros.search.views.SearchView;

public class SearchPresenter implements HttpServiceView {

    private SearchView searchView;

    public SearchPresenter(SearchView searchView) {
        this.searchView = searchView;
    }


    public void getSearchData() {
        new HttpServiceUtil(
                this,
                ApiEndPoints.top_atheltes.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.top_atheltes.getTag()
        ).execute();

        new HttpServiceUtil(this,
                ApiEndPoints.top_posts.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.top_posts.getTag()
        ).execute();

        new HttpServiceUtil(this,
                ApiEndPoints.atheltes.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.atheltes.getTag()
        ).execute();

    }


    @Override
    public void response(String response, int tag) {

        if (tag == ApiEndPoints.top_atheltes.getTag()) {
            try {
                AthletesMainModel athletesMainModel = JsonUtils.from(response, AthletesMainModel.class);
                searchView.updateTopPros(athletesMainModel.getAthletes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tag == ApiEndPoints.top_posts.getTag()) {
            try {

                HomeMainModel postsData = JsonUtils.from(response, HomeMainModel.class);
                searchView.updateTopPosts(postsData.getPosts());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (tag == ApiEndPoints.atheltes.getTag()) {
            try {
                FollowingModel followingModel = JsonUtils.from(response, FollowingModel.class);
                searchView.updateAllAthletes(followingModel.getAthletes());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(int tag) {

    }
}
