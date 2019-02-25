package pros.app.com.pros.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostModel implements Parcelable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("share_url")
    private String shareUrl;

    @JsonProperty("share_text")
    private String shareText;

    @JsonProperty("urls")
    private UrlModel urls;

    @JsonProperty("share_count")
    private int shareCount;

    @JsonProperty("athlete")
    private AthleteModel athlete;

    @JsonProperty("questioner")
    private AthleteModel questioner;

    @JsonProperty("likes")
    private LikeModel likes;

    @JsonProperty("hashtags")
    private List<HashtagModel> hashtags;

    @JsonProperty("mentions")
    private List<AthleteModel> mentions;

    @JsonProperty("reactions")
    private List<PostModel> reactions;

    @JsonProperty("comments")
    private List<PostModel> comments;


    public int getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareText() {
        return shareText;
    }

    public UrlModel getUrls() {
        return urls;
    }

    public int getShareCount() {
        return shareCount;
    }

    public AthleteModel getAthlete() {
        return athlete;
    }

    public LikeModel getLikes() {
        return likes;
    }

    public List<HashtagModel> getHashtags() {
        return hashtags;
    }

    public List<AthleteModel> getMentions() {
        return mentions;
    }

    public List<PostModel> getReactions() {
        return reactions;
    }

    public List<PostModel> getComments() {
        return comments;
    }

    public void setComments(List<PostModel> comments) {
        this.comments = comments;
    }

    public String getText() {
        return text;
    }

    public AthleteModel getQuestioner() {
        return questioner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.text);
        dest.writeString(this.contentType);
        dest.writeString(this.createdAt);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareText);
        dest.writeParcelable(this.urls, flags);
        dest.writeInt(this.shareCount);
        dest.writeParcelable(this.athlete, flags);
        dest.writeParcelable(this.questioner, flags);
        dest.writeParcelable(this.likes, flags);
        dest.writeList(this.hashtags);
        dest.writeList(this.mentions);
        dest.writeList(this.reactions);
        dest.writeList(this.comments);
    }

    public PostModel() {
    }

    protected PostModel(Parcel in) {
        this.id = in.readInt();
        this.text = in.readString();
        this.contentType = in.readString();
        this.createdAt = in.readString();
        this.shareUrl = in.readString();
        this.shareText = in.readString();
        this.urls = in.readParcelable(UrlModel.class.getClassLoader());
        this.shareCount = in.readInt();
        this.athlete = in.readParcelable(AthleteModel.class.getClassLoader());
        this.questioner = in.readParcelable(AthleteModel.class.getClassLoader());
        this.likes = in.readParcelable(LikeModel.class.getClassLoader());
        this.hashtags = new ArrayList<HashtagModel>();
        in.readList(this.hashtags, HashtagModel.class.getClassLoader());
        this.mentions = new ArrayList<AthleteModel>();
        in.readList(this.mentions, AthleteModel.class.getClassLoader());
        this.reactions = new ArrayList<PostModel>();
        in.readList(this.reactions, PostModel.class.getClassLoader());
        this.comments = new ArrayList<PostModel>();
        in.readList(this.comments, PostModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<PostModel> CREATOR = new Parcelable.Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel source) {
            return new PostModel(source);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };
}
