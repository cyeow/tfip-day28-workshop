package tfip.day28workshop.model.query;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class BoardGameReviews {
    private Integer gameId;
    private String name;
    private Integer year;
    private Integer rank;
    private BigDecimal average;
    private Integer usersRated;
    private String url;
    private String thumbnail;
    private List<String> reviews;
    private LocalDateTime timestamp;

    public BoardGameReviews() {
    }

    public BoardGameReviews(Integer gameId, String name, Integer year, Integer rank, BigDecimal average,
            Integer usersRated, String url, String thumbnail, List<String> reviews, LocalDateTime timestamp) {
        this.gameId = gameId;
        this.name = name;
        this.year = year;
        this.rank = rank;
        this.average = average;
        this.usersRated = usersRated;
        this.url = url;
        this.thumbnail = thumbnail;
        this.reviews = reviews;
        this.timestamp = timestamp;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public Integer getUsersRated() {
        return usersRated;
    }

    public void setUsersRated(Integer usersRated) {
        this.usersRated = usersRated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BoardGameReviews [gameId=" + gameId + ", name=" + name + ", year=" + year + ", rank=" + rank
                + ", average=" + average + ", usersRated=" + usersRated + ", url=" + url + ", thumbnail=" + thumbnail
                + ", reviews=" + reviews + ", timestamp=" + timestamp + "]";
    }


    public String toJsonString() {
        return toJson().toString();
    }

    public JsonObject toJson() {
        JsonArrayBuilder ab = Json.createArrayBuilder();
        getReviews().forEach(r -> ab.add(r));

        return Json.createObjectBuilder()
                .add("game_id", getGameId())
                .add("name", getName())
                .add("year", getYear())
                .add("rank", getRank())
                .add("average", getAverage())
                .add("users_rated", getUsersRated())
                .add("url", getUrl())
                .add("thumbnail", getThumbnail())
                .add("reviews", ab)
                .add("timestamp", getTimestamp().toString())
                .build();
    }

    public static BoardGameReviews create(String json) {
        return create(Json.createReader(new StringReader(json)).readObject());
    }

    public static BoardGameReviews create(JsonObject o) {
        List<String> reviews = new ArrayList<>();
        JsonArray arrReviews = o.getJsonArray("reviews");
        arrReviews.forEach(r -> reviews.add(r.toString()));

        return new BoardGameReviews(
            o.getInt("gid"),
            o.getString("name"),
            o.getInt("year"),
            o.getInt("rank"),
            o.getJsonNumber("average").bigDecimalValue(),
            o.getInt("users_rated"),
            o.getString("url"),
            o.getString("thumbnail"),
            reviews,
            LocalDateTime.now()
        );
    }
}
