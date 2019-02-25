package pros.app.com.pros.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AthleteModel implements Parcelable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("user_type")
    private String userType;

    @JsonProperty("avatar")
    private UrlModel avatar;

    @JsonProperty("followed_by_current_user")
    private boolean followedByCurrentUser;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserType() {
        return userType;
    }

    public UrlModel getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public boolean isFollowedByCurrentUser() {
        return followedByCurrentUser;
    }

    public void setFollowedByCurrentUser(boolean followedByCurrentUser) {
        this.followedByCurrentUser = followedByCurrentUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.userType);
        dest.writeParcelable((Parcelable) this.avatar, flags);
        dest.writeByte(this.followedByCurrentUser ? (byte) 1 : (byte) 0);
    }

    public AthleteModel() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true; //if both pointing towards same object on heap

        AthleteModel a = (AthleteModel) obj;
        return this.id == a.id;

    }

    protected AthleteModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.userType = in.readString();
        this.avatar = in.readParcelable(UrlModel.class.getClassLoader());
        this.followedByCurrentUser = in.readByte() != 0;
    }

    public static final Creator<AthleteModel> CREATOR = new Creator<AthleteModel>() {
        @Override
        public AthleteModel createFromParcel(Parcel source) {
            return new AthleteModel(source);
        }

        @Override
        public AthleteModel[] newArray(int size) {
            return new AthleteModel[size];
        }
    };
}
