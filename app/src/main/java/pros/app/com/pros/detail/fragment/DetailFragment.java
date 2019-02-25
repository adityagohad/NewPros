package pros.app.com.pros.detail.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.CustomDialogListener;
import pros.app.com.pros.base.DateUtils;
import pros.app.com.pros.base.KeyboardAction;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.create_post.activity.CreatePost;
import pros.app.com.pros.detail.adapter.CommentsAdapter;
import pros.app.com.pros.detail.adapter.MentionsAdapter;
import pros.app.com.pros.detail.adapter.ReactionAthlete;
import pros.app.com.pros.detail.presenter.DetailPresenter;
import pros.app.com.pros.detail.view.DetailView;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements DetailView, CustomDialogListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST_MODEL = "post_model";
    private static final String ARG_CONTENT_TYPE = "content_type";
    private static final String REACTIONS = "reactions";
    private static final String ANSWERS = "answers";

    private PostModel receivedPostModel;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.athlete_name)
    TextView athleteName;
    @BindView((R.id.created_at))
    TextView createdAt;
    @BindView(R.id.thumbnail_background)
    ImageView thumbnailBackground;
    @BindView(R.id.athlete_thumb)
    CircleImageView athleteThumb;
    @BindView(R.id.likes_count)
    TextView likesCount;
    @BindView(R.id.comment_count)
    TextView commentsCount;
    @BindView(R.id.comments_iv)
    ImageView commentsIcon;
    @BindView(R.id.question_container)
    ConstraintLayout questionContainer;
    @BindView(R.id.question_text)
    TextView questionText;
    @BindView(R.id.question_athlete_name)
    TextView questionAthleteName;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.athlete_list)
    RecyclerView athleteRecyclerview;
    @BindView(R.id.mentions_list)
    RecyclerView mentionsListRecyclerview;
    @BindView(R.id.tags_iv)
    ImageView mentionsIcon;

    @BindView(R.id.bsButtons)
    View bsButtons;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;

    @BindView(R.id.bsComments)
    View bsComments;
    @BindView(R.id.edtComments)
    EditText edtComments;
    @BindView(R.id.tvNumOfComments)
    TextView tvNumOfComments;
    @BindView(R.id.tvPost)
    TextView tvPost;
    @BindView(R.id.rv_comments)
    RecyclerView rvComments;

    @BindView(R.id.reaction_button)
    Button reactionButton;

    private BottomSheetBehavior behavior;

    boolean videoVisibleToUser = false;

    private int reactionVideoIndex = 0;
    private ArrayList<String> totalReactionsVideos = new ArrayList<>();
    private boolean toggleMentions = false;
    private DetailPresenter detailPresenter;
    private CommentsAdapter adapter;
    private int id;
    private int commentId;
    private int commentPosition;
    private String recievedContentType;
    private List<PostModel> reactionsList;
    private boolean processing;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postModel Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(PostModel postModel, String contentType) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST_MODEL, postModel);
        args.putString(ARG_CONTENT_TYPE, contentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receivedPostModel = getArguments().getParcelable(ARG_POST_MODEL);
            recievedContentType = getArguments().getString(ARG_CONTENT_TYPE);
        }
        detailPresenter = new DetailPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_layout, container, false);
        ButterKnife.bind(this, view);
        setupUI();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvComments.setLayoutManager(layoutManager);
        adapter = new CommentsAdapter(getContext(), receivedPostModel.getComments(), detailPresenter);
        rvComments.setAdapter(adapter);

    }

    private void setupUI() {
        String contentType = receivedPostModel.getContentType();
        String athleteFullName = "";
        String thumbnailUrl = "";
        String athleteThumbnailUrl = "";

        if (PrefUtils.isAthlete()) {
            if (REACTIONS.equalsIgnoreCase(recievedContentType)
                    || ANSWERS.equalsIgnoreCase(recievedContentType)) {
                reactionButton.setVisibility(View.GONE);
            } else {
                reactionButton.setVisibility(View.VISIBLE);
            }
        } else {
            reactionButton.setVisibility(View.GONE);
        }
        reactionsList = receivedPostModel.getReactions();
        if (reactionsList.size() > 0) {
            likesCount.setEnabled(true);
            for (int i = 0; i < reactionsList.size(); i++) {
                totalReactionsVideos.add(reactionsList.get(i).getUrls().getMobileUrl());
            }
        }

        if (contentType != null &&
                (contentType.equalsIgnoreCase("image") || contentType.equalsIgnoreCase("video"))) {
            thumbnailUrl = receivedPostModel.getUrls().getThumbnailUrl();
            athleteThumbnailUrl = receivedPostModel.getAthlete().getAvatar().getThumbnailUrl();
            athleteFullName = receivedPostModel.getAthlete().getFirstName() + " " + receivedPostModel.getAthlete().getLastName();
            commentsCount.setVisibility(View.VISIBLE);
            commentsIcon.setVisibility(View.VISIBLE);
            commentsCount.setText("" + receivedPostModel.getComments().size());

        } else if (receivedPostModel.getQuestioner() != null) {
            athleteFullName = receivedPostModel.getQuestioner().getName();
            thumbnailUrl = receivedPostModel.getQuestioner().getAvatar().getThumbnailUrl();
            athleteThumbnailUrl = receivedPostModel.getQuestioner().getAvatar().getThumbnailUrl();
            questionContainer.setVisibility(View.VISIBLE);
            questionText.setText(receivedPostModel.getText());
            questionAthleteName.setText(receivedPostModel.getQuestioner().getName());
            commentsCount.setVisibility(View.GONE);
            commentsIcon.setVisibility(View.GONE);
            thumbnailBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbnailBackground.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            if (totalReactionsVideos == null && totalReactionsVideos.isEmpty()) {
                likesCount.setEnabled(false);
            }
        }

        if (totalReactionsVideos != null && !totalReactionsVideos.isEmpty()) {
            playAllVideos();
        }

        String dateDifference = DateUtils.getDateDifference(receivedPostModel.getCreatedAt(), true);

        athleteName.setText(athleteFullName);
        Picasso.get().load(thumbnailUrl).into(thumbnailBackground);
        Picasso.get().load(athleteThumbnailUrl).into(athleteThumb);
        createdAt.setText(dateDifference);
        likesCount.setText(String.valueOf(receivedPostModel.getLikes().getCount()));
        if (receivedPostModel.getLikes().isLikedByCurrentUser()) {
            likesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
        } else {
            likesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
        }

        List<PostModel> reactionsList = receivedPostModel.getReactions();
        List<AthleteModel> mentionsList = receivedPostModel.getMentions();
        ArrayList<String> reactionUrlList = new ArrayList<>();

        if (reactionsList.size() > 0) {

            ArrayList<AthleteModel> athleteModels = new ArrayList<>();

            for (int i = 0; i < reactionsList.size(); i++) {
                athleteModels.add(reactionsList.get(i).getAthlete());
                reactionUrlList.add(reactionsList.get(i).getUrls().getMobileUrl());
            }

            ReactionAthlete reactionAthleteAdapter = new ReactionAthlete(athleteModels, reactionUrlList, this);

            athleteRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            athleteRecyclerview.setAdapter(reactionAthleteAdapter);

        }


        if (mentionsList.size() > 0) {
            mentionsIcon.setVisibility(View.VISIBLE);
            MentionsAdapter mentionsAdapter = new MentionsAdapter(mentionsList);

            mentionsListRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mentionsListRecyclerview.setAdapter(mentionsAdapter);
        }

        if (contentType != null && contentType.equalsIgnoreCase("video")) {
            if (receivedPostModel.getUrls().getMobileUrl() != null) {
                videoView.setVideoPath(receivedPostModel.getUrls().getMobileUrl());
                if (videoVisibleToUser) {
                    videoView.start();
                }
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        thumbnailBackground.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    private void playAllVideos() {
        if (reactionVideoIndex < totalReactionsVideos.size()) {
            videoView.setVideoPath(totalReactionsVideos.get(reactionVideoIndex));
            reactionVideoIndex++;
            if (videoVisibleToUser) {
                videoView.start();
            }
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    thumbnailBackground.setVisibility(View.GONE);
                    questionContainer.setVisibility(View.GONE);
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.stopPlayback();
                    videoView.suspend();
                    playAllVideos();
                }
            });
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser)   // If we are becoming invisible, then...
        {
            //pause or stop vide
            if (videoView != null) {
                videoView.stopPlayback();
            }
            videoVisibleToUser = false;
        }

        if (isVisibleToUser) // If we are becoming visible, then...
        {
            videoVisibleToUser = true;
            if (receivedPostModel != null) {
                String contentType = receivedPostModel.getContentType();

                List<PostModel> reactionsList = receivedPostModel.getReactions();
                if (reactionsList.size() > 0) {
                    for (int i = 0; i < reactionsList.size(); i++) {
                        totalReactionsVideos.add(reactionsList.get(i).getUrls().getMobileUrl());
                    }
                }

                //play your video
                if (receivedPostModel.getUrls() == null) {
                    return;
                }
                if (TextUtils.isEmpty(receivedPostModel.getUrls().getMobileUrl()))
                    return;

                if (contentType != null && contentType.equalsIgnoreCase("video") && videoView != null) {
                    videoView.setVideoPath(receivedPostModel.getUrls().getMobileUrl());
                    videoView.start();
                }
            }

            if (totalReactionsVideos != null && !totalReactionsVideos.isEmpty()) {
                playAllVideos();
            }
        }
    }

    @OnClick(R.id.tags_iv)
    void onMentionIconClick() {
        toggleMentions = !toggleMentions;
        if (toggleMentions) {
            mentionsListRecyclerview.setVisibility(View.VISIBLE);
        } else {
            mentionsListRecyclerview.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.share_iv)
    void shareDetails() {
        if (receivedPostModel != null) {

            String shareText = receivedPostModel.getShareText() + " " + receivedPostModel.getShareUrl();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");
            getActivity().startActivity(Intent.createChooser(sendIntent, "Send To"));
        }
    }

    @Override
    public void onLikeSuccess() {
        likesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
        likesCount.setText(String.valueOf(receivedPostModel.getLikes().getCount() + 1));
        receivedPostModel.getLikes().setLikedByCurrentUser(true);
        receivedPostModel.getLikes().setCount(receivedPostModel.getLikes().getCount() + 1);
        processing = false;
    }

    @Override
    public void onUnLikeSuccess() {
        if (receivedPostModel.getLikes().getCount() <= 0) {
            return;
        }
        likesCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
        likesCount.setText(String.valueOf(receivedPostModel.getLikes().getCount() - 1));
        receivedPostModel.getLikes().setLikedByCurrentUser(false);
        receivedPostModel.getLikes().setCount(receivedPostModel.getLikes().getCount() - 1);
        processing = false;
    }

    @Override
    public void onFailure(int message) {
        Toast.makeText(getContext(), getString(message), Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @OnClick(R.id.likes_count)
    public void onclickLike() {

        if (processing) {
            return;
        }

        processing = true;
        if (TextUtils.isEmpty(receivedPostModel.getContentType())) {
            if (reactionsList.size() > 0) {
                if (receivedPostModel.getLikes().isLikedByCurrentUser()) {
                    detailPresenter.unlikeQuestion(receivedPostModel.getId());
                } else {
                    detailPresenter.likeQuestion(receivedPostModel.getId());
                }
            }
        } else {
            if (receivedPostModel.getLikes().isLikedByCurrentUser()) {
                detailPresenter.unlikePost(receivedPostModel.getId());
            } else {
                detailPresenter.likePost(receivedPostModel.getId());
            }
        }
    }

    @OnClick(R.id.report_iv)
    public void onclickReport() {

        if (TextUtils.isEmpty(receivedPostModel.getContentType()))
            id = receivedPostModel.getQuestioner().getId();
        else
            id = receivedPostModel.getAthlete().getId();

        if (PrefUtils.getUser().getId() == id) {
            btn1.setText("Delete Post");
            btn1.setTextColor(ContextCompat.getColor(requireContext(), R.color.slection_blue));
        } else {
            btn1.setText("Report");
        }


        behavior = BottomSheetBehavior.from(bsButtons);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @OnClick(R.id.btn1)
    public void onClickbtn1() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (btn1.getText().equals("Delete Comment")) {
            detailPresenter.deleteComment(commentId);
        } else {
            confirmationDialog();
        }
    }

    @OnClick(R.id.btn2)
    public void onClickbtn2() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void confirmationDialog() {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        customDialogFragment.registerCallbackListener(this);
        Bundle bundle = new Bundle();
        if (PrefUtils.getUser().getId() == id) {
            bundle.putString("Title", "Delete Post");
            bundle.putString("Content", "Are you sure you want to delete this post?");
            bundle.putString("Action1", "Yes");
            bundle.putString("Action2", "Cancel");
        } else {
            bundle.putString("Title", getString(R.string.report_title));
            bundle.putString("Content", getString(R.string.report_content));
            bundle.putString("Action1", "Report");
            bundle.putString("Action2", "Cancel");
        }
        customDialogFragment.setArguments(bundle);
        customDialogFragment.show(getActivity().getSupportFragmentManager(), CustomDialogFragment.TAG);
    }

    @Override
    public void handleYes() {
        if (PrefUtils.getUser().getId() == id) {
            if (TextUtils.isEmpty(receivedPostModel.getContentType())) {
                detailPresenter.deleteQuestion(receivedPostModel.getId());
            } else {
                detailPresenter.deletePost(receivedPostModel.getId());
            }
        } else {
            detailPresenter.flagPost(receivedPostModel.getId());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        emailAlertForMe();
                        emailAlertToAdmins();
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
    }

    @OnClick(R.id.reaction_button)
    void createReaction() {
        PrefUtils.putString("LAST_SCREEN", DetailFragment.class.getName());
        if (TextUtils.isEmpty(receivedPostModel.getContentType()))
            PrefUtils.putString("CONTENT_TYPE", "answer");
        else
            PrefUtils.putString("CONTENT_TYPE", "reaction");
        PrefUtils.putInt("PARENT_ID", receivedPostModel.getId());
        Intent createPostIntent = new Intent(getActivity(), CreatePost.class);
        startActivity(createPostIntent);
    }

    @Override
    public void handleNo() {

    }

    @Override
    public void onflagPostSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString("Title", "Success");
        bundle.putString("Content", getString(R.string.reported_content));
        bundle.putString("Action2", "Close");
        CustomDialogFragment.newInstance(bundle).show(getActivity().getSupportFragmentManager(), CustomDialogFragment.TAG);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @OnClick({R.id.comments_iv, R.id.comment_count})
    public void onclickComment() {

        bsComments.setVisibility(View.VISIBLE);
        videoView.pause();

        if (!PrefUtils.isAthlete() && receivedPostModel.getComments() == null || receivedPostModel.getComments().size() == 0) {
            tvNumOfComments.setText("Beat everyone to the punch!\nBe the first to comment...");
        } else {
            tvNumOfComments.setText(receivedPostModel.getComments().size() + " Comments");
        }

        if (PrefUtils.isAthlete()) {
            edtComments.setVisibility(View.VISIBLE);
            edtComments.addTextChangedListener(watcher);
            edtComments.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    tvPost.setVisibility(View.VISIBLE);
                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(edtComments, 0);
                    edtComments.requestFocus();
                    return false;
                }
            });
        } else {
            edtComments.setVisibility(View.GONE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
    }

    @OnClick(R.id.tvPost)
    public void onClickPostcomment() {
        if (TextUtils.isEmpty(edtComments.getText().toString())) {
            Toast.makeText(edtComments.getContext(), "Enter some Comment", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("commentable_type", "Post");
                jsonRequest.put("commentable_id", receivedPostModel.getId());
                jsonRequest.put("text", edtComments.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            detailPresenter.postComment(jsonRequest.toString());
        }
    }

    @OnClick({R.id.ivDownArrow, R.id.view_top})
    public void onClickDownArrow() {
        bsComments.setVisibility(View.GONE);
        videoView.start();
    }

    @Override
    public void onPostingComment(PostModel commentModel) {
        receivedPostModel.setComments(commentModel.getComments());
        adapter.setData(receivedPostModel.getComments());
        adapter.notifyDataSetChanged();
        edtComments.setText("");
        tvPost.setVisibility(View.GONE);
        tvNumOfComments.setText(receivedPostModel.getComments().size() + " Comments");
        commentsCount.setText("" + receivedPostModel.getComments().size());
        KeyboardAction.hideSoftKeyboard(getActivity(), edtComments);
    }

    @Override
    public void onClickComment(int id, int position) {

        if (PrefUtils.getUser().getId() != receivedPostModel.getComments().get(position).getAthlete().getId()) {
            return;
        }

        commentPosition = position;
        commentId = id;
        btn1.setText("Delete Comment");

        behavior = BottomSheetBehavior.from(bsButtons);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        edtComments.setVisibility(View.GONE);
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        edtComments.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void playVideo(String url) {
        videoView.setVideoPath(url);
        if (videoVisibleToUser) {
            videoView.start();
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                thumbnailBackground.setVisibility(View.GONE);
                questionContainer.setVisibility(View.GONE);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.stopPlayback();
                videoView.suspend();
                playAllVideos();
            }
        });
    }

    @Override
    public void onDeletingComment() {
        receivedPostModel.getComments().remove(commentPosition);
        adapter.setData(receivedPostModel.getComments());
        adapter.notifyDataSetChanged();

        if (!PrefUtils.isAthlete() && receivedPostModel.getComments() == null || receivedPostModel.getComments().size() == 0) {
            tvNumOfComments.setText("Beat everyone to the punch!\nBe the first to comment...");
        } else {
            tvNumOfComments.setText(receivedPostModel.getComments().size() + " Comments");
        }
        commentsCount.setText("" + receivedPostModel.getComments().size());
    }

    public void emailAlertForMe() throws UnirestException {

        Unirest.post("https://api.mailgun.net/v3/sandbox180e9f38e85f4f4da7e6345a49a932c8.mailgun.org/messages")
                .basicAuth("api", "5502304ca6a7b066c8d99cc300bc74cd-059e099e-aa469954")
                .field("from", "admin@pros-staging.com")
                .field("to", "adityagohad@gmail.com")
                .field("subject", "Alert")
                .field("text", "Koi report abuse maaar diye!!!\n" + "The following post was marked as objectionable by user\n" + receivedPostModel.getShareUrl().replace("https", "http")).asStringAsync(new Callback<String>() {
            @Override
            public void completed(HttpResponse<String> httpResponse) {
                Log.e("Email", "Sent");
            }

            @Override
            public void failed(UnirestException e) {
                Log.e("Email", e.getMessage());
            }

            @Override
            public void cancelled() {
                Log.e("Email", "ooops!! something went wrong");
            }
        });
    }

    public void emailAlertToAdmins() throws UnirestException {

        Unirest.post("https://api.mailgun.net/v3/sandbox180e9f38e85f4f4da7e6345a49a932c8.mailgun.org/messages")
                .basicAuth("api", "5502304ca6a7b066c8d99cc300bc74cd-059e099e-aa469954")
                .field("from", "admin@pros-staging.com")
                .field("to", "pranalee.navalkar@gmail.com")
                .field("subject", "Alert")
                .field("text", "The following post was marked as objectionable by user\n" + receivedPostModel.getShareUrl().replace("https", "http")).asStringAsync(new Callback<String>() {
            @Override
            public void completed(HttpResponse<String> httpResponse) {
                Log.e("Email", "Sent");
            }

            @Override
            public void failed(UnirestException e) {
                Log.e("Email", e.getMessage());
            }

            @Override
            public void cancelled() {
                Log.e("Email", "ooops!! something went wrong");
            }
        });

        Unirest.post("https://api.mailgun.net/v3/sandbox180e9f38e85f4f4da7e6345a49a932c8.mailgun.org/messages")
                .basicAuth("api", "5502304ca6a7b066c8d99cc300bc74cd-059e099e-aa469954")
                .field("from", "admin@pros-staging.com")
                .field("to", "brandon@jaychrismgmt.com")
                .field("subject", "Alert")
                .field("text", "The following post was marked as objectionable by user\n" + receivedPostModel.getShareUrl().replace("https", "http")).asStringAsync(new Callback<String>() {
            @Override
            public void completed(HttpResponse<String> httpResponse) {
                Log.e("Email", "Sent");
            }

            @Override
            public void failed(UnirestException e) {
                Log.e("Email", e.getMessage());
            }

            @Override
            public void cancelled() {
                Log.e("Email", "ooops!! something went wrong");
            }
        });

    }

}
