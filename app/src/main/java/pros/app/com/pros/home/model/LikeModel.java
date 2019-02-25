package pros.app.com.pros.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikeModel implements Parcelable {

    @JsonProperty("count")
    private int count;
    @JsonProperty("liked_by_current_user")
    private boolean likedByCurrentUser;

    public int getCount() {
        return count;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeByte(this.likedByCurrentUser ? (byte) 1 : (byte) 0);
    }

    public LikeModel() {
    }

    protected LikeModel(Parcel in) {
        this.count = in.readInt();
        this.likedByCurrentUser = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LikeModel> CREATOR = new Parcelable.Creator<LikeModel>() {
        @Override
        public LikeModel createFromParcel(Parcel source) {
            return new LikeModel(source);
        }

        @Override
        public LikeModel[] newArray(int size) {
            return new LikeModel[size];
        }
    };
}
