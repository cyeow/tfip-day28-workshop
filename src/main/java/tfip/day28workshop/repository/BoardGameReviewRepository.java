package tfip.day28workshop.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Avg;
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import tfip.day28workshop.model.query.BoardGameReviews;
import tfip.day28workshop.model.query.GameWithReview;

@Repository
public class BoardGameReviewRepository {

        @Autowired
        private MongoTemplate mongo;

        public BoardGameReviews getGameReviews(String gameId) {

                Aggregation agg = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where("gid").is(Integer.parseInt(gameId))),
                                Aggregation.lookup("reviews", "gid", "gid", "reviews"),
                                Aggregation.project()
                                                .and("gid").as("gid")
                                                .and("name").as("name")
                                                .and("year").as("year")
                                                .and("ranking").as("rank")
                                                .and(Avg.avgOf("$reviews.rating"))
                                                .as("average")
                                                .and("users_rated").as("users_rated")
                                                .and("url").as("url")
                                                .and("image").as("thumbnail")
                                                .and(
                                                        Map.itemsOf("$reviews._id")
                                                           .as("review_id")
                                                           .andApply(
                                                                new AggregationExpression() {
                                                                        @Override
                                                                        public Document toDocument(AggregationOperationContext context) {
                                                                                return new Document(
                                                                                        "$concat",
                                                                                        Arrays.asList("/review/", new Document("$toString", "$$review_id")));
                                                                        }
                                                                }
                                                        )
                                                )
                                                .as("reviews"));
                // .andExpression("ISODate()")
                // .as("timestamp"));
                
                AggregationResults<Document> results = mongo.aggregate(
                                agg, "games", Document.class);

                List<Document> resultList = results.getMappedResults();

                // there should only be one document in the result
                if (resultList.size() != 1) {
                        return null;
                }
                return BoardGameReviews.create(resultList.get(0).toJson());
        }

        public List<GameWithReview> getRatingByGame(String filterBy) {
                // can limit the documents if this takes too long to test
                Aggregation agg = Aggregation.newAggregation(
                        //        Aggregation.limit(1000),
                                Aggregation.sort(Sort.Direction.DESC, "rating"),
                                Aggregation.group("gid").first(Aggregation.ROOT).as("doc"),
                                Aggregation.replaceRoot("doc"),
                                Aggregation.lookup("games", "gid", "gid", "games"),
                                Aggregation.unwind("games"),
                                Aggregation.project()
                                                .and("gid").as("_id")
                                                .and("games.name").as("name")
                                                .and("rating").as("rating")
                                                .and("user").as("user")
                                                .and("c_text").as("comment")
                                                .and("c_id").as("review_id"));

                AggregationResults<Document> results = mongo.aggregate(
                                agg, "reviews", Document.class);

                List<Document> resultList = results.getMappedResults();
                List<GameWithReview> result = new ArrayList<>();
                resultList.forEach(r -> result.add(GameWithReview.create(r.toJson())));

                return result;
        }

}
