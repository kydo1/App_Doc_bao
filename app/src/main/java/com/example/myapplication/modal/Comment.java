package com.example.myapplication.modal;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class Comment {
    private  String _id;
    private  String text;
    private  String storyId;
    private  String userId;
    private Date createdAt;
    private  Date updatedAt;
    private  String avatar;
    private  String fullName;
    private  String nameParentComment;
    private ArrayList<String> replies;

    public Comment(String _id, String text, String storyId, String userId, Date createdAt, Date updatedAt, String avatar, String fullName, String nameParentComment, ArrayList<String> replies) {
        this._id = _id;
        this.text = text;
        this.storyId = storyId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.avatar = avatar;
        this.fullName = fullName;
        this.nameParentComment = nameParentComment;
        this.replies = replies;
    }

    public Comment(String _id, String text, String storyId, String userId, Date createdAt, Date updatedAt, String avatar, String fullName, ArrayList<String> replies) {
        this._id = _id;
        this.text = text;
        this.storyId = storyId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.avatar = avatar;
        this.fullName = fullName;
        this.replies = replies;
    }

    public Comment() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String get_id() {
        return _id;
    }


    public ArrayList<String> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<String> replies) {
        this.replies = replies;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNameParentComment() {
        return nameParentComment;
    }

    public void setNameParentComment(String nameParentComment) {
        this.nameParentComment = nameParentComment;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", text='" + text + '\'' +
                ", storyId='" + storyId + '\'' +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", avatar='" + avatar + '\'' +
                ", fullName='" + fullName + '\'' +
                ", nameParentComment='" + nameParentComment + '\'' +
                ", replies=" + replies +
                '}';
    }
}
