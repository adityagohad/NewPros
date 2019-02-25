package pros.app.com.pros.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import pros.app.com.pros.base.LinkModel;
import pros.app.com.pros.home.model.AthleteModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowingModel {

    private LinkModel links;
    private List<AthleteModel> athletes;

    public List<AthleteModel> getAthletes() {
        return athletes;
    }

    public LinkModel getLinks() {
        return links;
    }
}
