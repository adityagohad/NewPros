package pros.app.com.pros.detail.view;

import pros.app.com.pros.home.model.PostModel;

public interface DetailView {

    void onLikeSuccess();

    void onUnLikeSuccess();

    void onflagPostSuccess();

    void onPostingComment(PostModel commentModel);

    void onDeletingComment();

    void onFailure(int message);

    void onClickComment(int id, int adapterPosition);

    void playVideo(String url);
}
