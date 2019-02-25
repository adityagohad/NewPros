package pros.app.com.pros.search.views;

import java.util.ArrayList;
import java.util.List;

import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;

public interface SearchView {
    void updateTopPros(ArrayList<AthleteModel> topProsList);

    void updateTopPosts(ArrayList<PostModel> topPostsList);

    void updateAllAthletes(List<AthleteModel> allAthleteList);
}
