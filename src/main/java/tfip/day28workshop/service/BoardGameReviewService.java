package tfip.day28workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.day28workshop.model.query.BoardGameReviews;
import tfip.day28workshop.model.query.GameWithReview;
import tfip.day28workshop.repository.BoardGameReviewRepository;

@Service
public class BoardGameReviewService {

    @Autowired
    BoardGameReviewRepository repo;
    
    public BoardGameReviews getGameReviews(String gameId) {
        return repo.getGameReviews(gameId);
    }

    public List<GameWithReview> getRatingByGame(String filterBy) {
        return repo.getRatingByGame(filterBy);
    }
    
}
