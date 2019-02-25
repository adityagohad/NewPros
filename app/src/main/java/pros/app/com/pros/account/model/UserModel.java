package pros.app.com.pros.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    @JsonProperty("id")
    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("api_key")
    private String apiKey;
    @JsonProperty("globalId")
    private String global_id;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("account_status")
    private String accountStatus;
    @JsonProperty("user_type")
    private String userType;
    @JsonProperty("avatar")
    private AvatarModel avatar;

    private String thumbUrl;
    private String mediumUrl;
    private String originalUrl;

    public UserModel() {
    }

    public UserModel(int id, String email, String firstName, String lastName, String apiKey, String userType, String thumbUrl, String mediumUrl, String originalUrl) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.apiKey = apiKey;
        this.userType = userType;
        this.thumbUrl = thumbUrl;
        this.mediumUrl = mediumUrl;
        this.originalUrl = originalUrl;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getGlobal_id() {
        return global_id;
    }

    public boolean isActive() {
        return active;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getUserType() {
        return userType;
    }

    public AvatarModel getAvatar() {
        return avatar;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
