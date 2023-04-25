package tfip.day28workshop.model.query;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class GameWithReview {
    private Integer _id;
    private String name;
    private Integer rating;
    private String user;
    private String comment;
    private String reviewId;

    public GameWithReview() {
    }

    public GameWithReview(Integer _id, String name, Integer rating, String user, String comment, String reviewId) {
        this._id = _id;
        this.name = name;
        this.rating = rating;
        this.user = user;
        this.comment = comment;
        this.reviewId = reviewId;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "GameWithReview [_id=" + _id + ", name=" + name + ", rating=" + rating + ", user=" + user + ", comment="
                + comment + ", reviewId=" + reviewId + "]";
    }

    public String toJsonString() {
        return toJson().toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("_id", get_id())
                .add("name", getName())
                .add("rating", getRating())
                .add("user", getUser())
                .add("comment", getComment())
                .add("review_id", getReviewId())
                .build();
    }

    public static GameWithReview create(String json) {
        return create(Json.createReader(new StringReader(json)).readObject());
    }

    public static GameWithReview create(JsonObject o) {
        return new GameWithReview(
            o.getInt("_id"),
            o.getString("name"),
            o.getInt("rating"), 
            o.getString("user"),
            o.getString("comment"),
            o.getString("review_id"));
    }
}
