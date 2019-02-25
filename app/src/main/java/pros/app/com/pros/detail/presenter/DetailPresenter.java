package pros.app.com.pros.detail.presenter;

import java.io.IOException;

import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.detail.view.DetailView;
import pros.app.com.pros.home.model.PostModel;

public class DetailPresenter implements HttpServiceView {

    private DetailView detailView;
    private PostModel commentModel;

    public DetailPresenter(DetailView detailView) {
        this.detailView = detailView;
    }

    public void likePost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.like_post.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.like_post.getTag()
        ).execute();

    }

    public void unlikePost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.unlike_post.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.unlike_post.getTag()
        ).execute();
    }

    public void likeQuestion(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.like_question.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.like_question.getTag()
        ).execute();
    }

    public void unlikeQuestion(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.unlike_question.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.unlike_question.getTag()
        ).execute();
    }

    public void flagPost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.flag_post.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.flag_post.getTag()
        ).execute();
    }

    public void deletePost(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.delete_post.getApi(), id),
                ProsConstants.DELETE_METHOD,
                null,
                ApiEndPoints.delete_post.getTag()
        ).execute();
    }

    public void deleteQuestion(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.delete_question.getApi(), id),
                ProsConstants.DELETE_METHOD,
                null,
                ApiEndPoints.delete_question.getTag()
        ).execute();
    }

    public void postComment(String jsonRequest) {
        new HttpServiceUtil(
                this,
                ApiEndPoints.post_comment.getApi(),
                ProsConstants.POST_METHOD,
                jsonRequest,
                ApiEndPoints.post_comment.getTag()
        ).execute();
    }

    public void deleteComment(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.delete_comment.getApi(), id),
                ProsConstants.DELETE_METHOD,
                null,
                ApiEndPoints.delete_comment.getTag()
        ).execute();
    }


    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.like_question.getTag() || tag == ApiEndPoints.like_post.getTag()) {
            detailView.onLikeSuccess();
        } else if (tag == ApiEndPoints.unlike_post.getTag() || tag == ApiEndPoints.unlike_question.getTag()) {
            detailView.onUnLikeSuccess();
        } else if (tag == ApiEndPoints.flag_post.getTag()) {
            detailView.onflagPostSuccess();
        } else if (tag == ApiEndPoints.post_comment.getTag()) {
            try {
                commentModel = JsonUtils.from(response, PostModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            detailView.onPostingComment(commentModel);
        } else if (tag == ApiEndPoints.delete_comment.getTag()) {
            detailView.onDeletingComment();
        }
    }

    @Override
    public void onError(int tag) {
        detailView.onFailure(R.string.internal_error);
    }

    public void onClickingComment(int id, int adapterPosition) {
        detailView.onClickComment(id, adapterPosition);
    }
}
