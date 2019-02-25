package pros.app.com.pros.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaDataModel {

    @JsonProperty("posts_count")
    private int postsCount;
    @JsonProperty("reactions_count")
    private int reactionsCount;
    @JsonProperty("followers_count")
    private int followersCount;
    @JsonProperty("follow_count")
    private int followCount;
    @JsonProperty("questions_asked_count")
    private int questionsAskedCount;
    @JsonProperty("questions_answered_count")
    private int questionsAnsweredCount;
    @JsonProperty("liked_posts_count")
    private int likedPostsCount;
    @JsonProperty("liked_questions_count")
    private int likedQuestionsCount;

    public int getPostsCount() {
        return postsCount;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public int getQuestionsAskedCount() {
        return questionsAskedCount;
    }

    public int getQuestionsAnsweredCount() {
        return questionsAnsweredCount;
    }

    public int getLikedPostsCount() {
        return likedPostsCount;
    }

    public int getLikedQuestionsCount() {
        return likedQuestionsCount;
    }
}
