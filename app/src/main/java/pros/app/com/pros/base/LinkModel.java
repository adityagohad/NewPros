package pros.app.com.pros.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkModel {

    private String next;
    private String previous;

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }
}
