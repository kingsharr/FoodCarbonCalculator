package com.example.madtest1;

import java.util.ArrayList;

class Comment{
    private String commentText;
    private String userId;

    public Comment(String commentText, String userId) {
        this.commentText = commentText;
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

public class Post {

    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String userID;
    private String userPhoto;
    private ArrayList<Comment> commentList;
    private String postId;


    public Post(String postId, String title, String description, String picture, String userID, String userPhoto){
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userID = userID;
        this.userPhoto = userPhoto;
        this.commentList = new ArrayList<Comment>();
    }

    public Post(){

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(Comment comment) {
        this.commentList.add(comment);
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

}