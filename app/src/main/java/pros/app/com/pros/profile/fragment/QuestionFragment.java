package pros.app.com.pros.profile.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pros.app.com.pros.R;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.adapter.LikedQuestionsAdapter;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.AthleteProfilePresenter;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment implements ProfileView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DATA_TYPE = "data_type";
    private static final String ID = "id";

    // TODO: Rename and change types of parameters
    private String dataType;
    private int id;

    RecyclerView likedQuestionsRecyclerview;

    private ProfilePresenter profilePresenter;
    private AthleteProfilePresenter athleteProfilePresenter;
    private LikedQuestionsAdapter likedQuestionsAdapter;
    private TextView labelNothing;
    private ImageView emptyStateiv;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dataType Parameter 1.
     * @param id       Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String dataType, int id) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(DATA_TYPE, dataType);
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePresenter = new ProfilePresenter(this);
        athleteProfilePresenter = new AthleteProfilePresenter(this);
        if (getArguments() != null) {
            dataType = getArguments().getString(DATA_TYPE);
            id = getArguments().getInt(ID);
        }

        if (dataType.equalsIgnoreCase("athlete_questions")) {
            athleteProfilePresenter.getQuestionsData(id);
        } else if (dataType.equalsIgnoreCase("liked_questions")) {
            profilePresenter.getLikedQuestionsData();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        labelNothing = view.findViewById(R.id.label_nothing);
        emptyStateiv = view.findViewById(R.id.empty_state_iv);
        likedQuestionsRecyclerview = view.findViewById(R.id.liked_questions);
        likedQuestionsRecyclerview.setNestedScrollingEnabled(false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {
        if (postsList != null && !postsList.isEmpty()) {
            labelNothing.setVisibility(View.GONE);
            emptyStateiv.setVisibility(View.GONE);
            likedQuestionsRecyclerview.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            likedQuestionsRecyclerview.setLayoutManager(layoutManager);
            likedQuestionsAdapter = new LikedQuestionsAdapter(getActivity(), postsList);
            likedQuestionsRecyclerview.setAdapter(likedQuestionsAdapter);
        } else {
            if (dataType.equalsIgnoreCase("athlete_questions")) {
                labelNothing.setText("No Questions Yet");
            } else if (dataType.equalsIgnoreCase("liked_questions")) {
                labelNothing.setText("No Liked Questions Yet");
            }

        }
    }

    @Override
    public void updateLikedPosts(ArrayList<PostModel> postList) {

    }

    @Override
    public void onsucessUnfollow() {
        //Nothing to do here.
    }

    @Override
    public void onSuccessFollow() {
        //Nothing to do here.
    }

    @Override
    public void onSuccessBlock() {
        //Nothing to do here.
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
}
