package pros.app.com.pros.create_post.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoPathModel {

    @JsonProperty("upload_url")
    private String uploadUrl;
    @JsonProperty("video_guid")
    private String videoGuid;

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getVideoGuid() {
        return videoGuid;
    }
}
