package pros.app.com.pros.home.view;

import java.util.ArrayList;

import pros.app.com.pros.home.model.PostModel;

public interface HomeView {

    void bindData(ArrayList<PostModel> postsList);

    void updateHomeScreen(ArrayList<PostModel> postsList);
}
