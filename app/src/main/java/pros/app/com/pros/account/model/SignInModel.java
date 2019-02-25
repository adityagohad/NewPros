package pros.app.com.pros.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInModel {

    @JsonProperty("fan")
    private UserModel fan;

    @JsonProperty("athlete")
    private UserModel athlete;

    public UserModel getFan() {
        return fan;
    }

    public UserModel getAthlete() {
        return athlete;
    }
}
