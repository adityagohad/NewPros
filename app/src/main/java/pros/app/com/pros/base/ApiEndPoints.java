package pros.app.com.pros.base;

public enum ApiEndPoints {

    sign_in("/sign-in", 1),
    fb_sign_in("/sign-in/facebook/create", 2),
    sign_out("/sign-out", 3),
    sign_up("/sign-up", 4),
    deactivate("/deactivate", 5),
    me("/me", 6),
    forgot_password("/passwords", 7),
    update_me("/update_me", 8),

    list_posts("/posts", 9),
    post_content("/posts", 10),
    top_posts("/posts/top", 11),
    select_post("/posts/%s", 12),
    update_post("/posts/%s", 13),
    delete_post("/posts/%s", 14),
    like_post("/posts/%s/like", 15),
    unlike_post("/posts/%s/unlike", 16),

    questions("/questions", 17),
    post_question("/questions", 18),
    select_question("/questions/%s", 19),
    update_question("/questions/%s", 20),
    delete_question("/questions/%s", 21),
    like_question("/questions/%s/like", 22),
    unlike_question("/questions/%s/unlike", 23),

    post_comment("/comments", 24),
    delete_comment("/comments/%s", 25),

    upload_video("/videos/new", 26),
    upload_image("/posts/image_upload_url", 27),
    upload_avatar("/avatars/new", 28),

    atheltes("/athletes", 29),
    search_atheltes("/athletes/search", 30),
    top_atheltes("/athletes/top", 31),
    follow_atheltes("/athletes/%s/follow", 32),
    unfollow_atheltes("/athletes/%s/unfollow", 33),
    select_atheltes("/athletes/%s", 34),
    followers("/athletes/%s/followers", 35),
    pro_following("/athletes/%s/following", 36),
    fan_following("/fans/%s/following", 37),

    pros_posts("/athletes/%s/posts", 38),
    pros_reactions("/athletes/%s/reactions", 39),
    pros_questions("/athletes/%s/questions", 40),
    pros_answers("/athletes/%s/answers", 41),

    fans_liked_posts("/fans/%s/liked_posts", 42),
    fans_liked_questions("/fans/%s/liked_questions", 43),

    pros_profile_metadata("/athletes/%s/metadata", 44),
    fans_profile_metadata("/fans/%s/metadata", 45),

    post_invite("/users/send_invite", 46),

    flag_post("/posts/%s/flag", 47),

    block_user("/users/block_user", 48),

    upload_url_to_db("upload_url_to_db", 49);

    //private static final String BASE_URL = "http://pros-production.herokuapp.com/api/v1";
    private static final String BASE_URL = "http://pros-staging.herokuapp.com/api/v1";
    private final String api;
    private final int tag;

    ApiEndPoints(String api, int tag) {
        this.api = api;
        this.tag = tag;
    }

    public String getApi() {
        return String.format("%s%s", BASE_URL, api);
    }

    public int getTag() {
        return tag;
    }

}
