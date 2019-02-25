package pros.app.com.pros.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeMainModel {


    private ArrayList<PostModel> posts;
    private ArrayList<PostModel> questions;
    private ArrayList<PostModel> answers;

    public ArrayList<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostModel> posts) {
        this.posts = posts;
    }

    public ArrayList<PostModel> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<PostModel> questions) {
        this.questions = questions;
    }

    public ArrayList<PostModel> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<PostModel> answers) {
        this.answers = answers;
    }
}
