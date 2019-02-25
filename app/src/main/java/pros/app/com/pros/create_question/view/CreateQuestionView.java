package pros.app.com.pros.create_question.view;

import java.util.List;

import pros.app.com.pros.home.model.AthleteModel;

public interface CreateQuestionView {
    void updateAthletesData(List<AthleteModel> athletes);

    void closeActivity();

    void showLoader();

    void showPostErrorMessage();
}
