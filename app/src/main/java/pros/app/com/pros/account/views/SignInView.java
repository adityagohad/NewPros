package pros.app.com.pros.account.views;

import pros.app.com.pros.account.model.SignInModel;

public interface SignInView {

    void onSucess(SignInModel signInModel);

    void onFailure(String message);

    void onValidationError(int message);

    void onSucessforgotPswd();
}
