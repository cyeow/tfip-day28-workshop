package tfip.day28workshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import tfip.day28workshop.model.query.BoardGameReviews;
import tfip.day28workshop.model.query.GameWithReview;
import tfip.day28workshop.service.BoardGameReviewService;

@RestController
public class BoardGameReviewRestController {

    @Autowired
    BoardGameReviewService svc;

    @GetMapping(path = "/game/{gameId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBoardGameReviews(@PathVariable String gameId) {

        BoardGameReviews result = svc.getGameReviews(gameId);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("Invalid game id " + gameId + "."));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toJsonString());
    }

    @GetMapping(path = "/games/{filterBy}")
    public ResponseEntity<String> getRatingByGame(@PathVariable String filterBy) {
        if (!filterBy.equalsIgnoreCase("highest") && !filterBy.equalsIgnoreCase("lowest")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateMsgJsonString("Invalid rating order (indicate highest or lowest)."));
        }

        List<GameWithReview> result = svc.getRatingByGame(filterBy);

        JsonArrayBuilder ab = Json.createArrayBuilder();
        result.forEach(r -> ab.add(r.toJson()));

        String response = Json.createObjectBuilder()
                .add("rating", filterBy)
                .add("games", ab)
                .add("timestamp", LocalDateTime.now().toString())
                .build().toString();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

    private String generateMsgJsonString(String message) {
        return Json.createObjectBuilder()
                .add("message", message)
                .build().toString();
    }
}
