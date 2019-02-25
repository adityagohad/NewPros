package pros.app.com.pros.search.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import pros.app.com.pros.home.model.AthleteModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AthletesMainModel {

    public ArrayList<AthleteModel> getAthletes() {
        return athletes;
    }

    public void setAthletes(ArrayList<AthleteModel> athletes) {
        this.athletes = athletes;
    }

    private ArrayList<AthleteModel> athletes;
}
