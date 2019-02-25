package pros.app.com.pros.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadUrlModel {

    @JsonProperty("upload_url")
    private String uploadUrl;
    @JsonProperty("image_upload_url")
    private String imageUploadUrl;
    @JsonProperty("guid")
    private String guid;

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getGuid() {
        return guid;
    }

    public String getImageUploadUrl() {
        return imageUploadUrl;
    }
}
