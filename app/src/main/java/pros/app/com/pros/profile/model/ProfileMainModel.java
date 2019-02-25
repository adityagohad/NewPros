package pros.app.com.pros.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileMainModel {

    @JsonProperty("metadata")
    private MetaDataModel metadata;

    public MetaDataModel getMetadata() {
        return metadata;
    }
}
