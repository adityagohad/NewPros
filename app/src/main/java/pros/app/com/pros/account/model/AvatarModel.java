package pros.app.com.pros.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvatarModel {

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    @JsonProperty("medium_url")
    private String mediumUrl;
    @JsonProperty("original_url")
    private String originalUrl;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
