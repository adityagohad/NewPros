package pros.app.com.pros.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlModel implements Parcelable {

    @JsonProperty("mobile_url")
    private String mobileUrl;

    @JsonProperty("intro_url")
    private String introUrl;

    @JsonProperty("share_url")
    private String shareUrl;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("large_url")
    private String largeUrl;

    @JsonProperty("x_large_url")
    private String xLargeUrl;

    @JsonProperty("original_url")
    private String originalUrl;

    @JsonProperty("medium_url")
    private String mediumUrl;


    public String getMobileUrl() {
        return mobileUrl;
    }

    public String getIntroUrl() {
        return introUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public String getxLargeUrl() {
        return xLargeUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mobileUrl);
        dest.writeString(this.introUrl);
        dest.writeString(this.shareUrl);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.largeUrl);
        dest.writeString(this.xLargeUrl);
        dest.writeString(this.originalUrl);
        dest.writeString(this.mediumUrl);
    }

    public UrlModel() {
    }

    protected UrlModel(Parcel in) {
        this.mobileUrl = in.readString();
        this.introUrl = in.readString();
        this.shareUrl = in.readString();
        this.thumbnailUrl = in.readString();
        this.largeUrl = in.readString();
        this.xLargeUrl = in.readString();
        this.originalUrl = in.readString();
        this.mediumUrl = in.readString();
    }

    public static final Parcelable.Creator<UrlModel> CREATOR = new Parcelable.Creator<UrlModel>() {
        @Override
        public UrlModel createFromParcel(Parcel source) {
            return new UrlModel(source);
        }

        @Override
        public UrlModel[] newArray(int size) {
            return new UrlModel[size];
        }
    };
}
