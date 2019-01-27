package gr.uoa.di.dbm.restapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class CitizenRepositoryImpl implements CitizenRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CitizenRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<String> query8(){

        SortOperation sortByUpvotes = Aggregation.sort(new Sort(Sort.Direction.DESC, "votes"));
        LimitOperation limitToOnlyFifty = Aggregation.limit(50);


        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("citizenId")
                .andExpression("name").as("name")
                .andExpression("phone").as("phone")
                .andExpression("votes").as("numberOfUpvotes")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(sortByUpvotes,limitToOnlyFifty,projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "citizen", String.class);

        return result.getMappedResults();
    }

    public List<String> query9(){
        UnwindOperation unwindRequests = Aggregation.unwind("upvotedRequests");
        GroupOperation groupObject = Aggregation.group("_id","name","phone")
                .addToSet("upvotedRequests.ward").as("distset");

        SortOperation sortByWards = Aggregation.sort(new Sort(Sort.Direction.DESC, "numberOfDistWards"));
        LimitOperation limitToOnlyFifty = Aggregation.limit(50);

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id._id").as("citizenId")
                .andExpression("_id.name").as("name")
                .andExpression("_id.phone").as("phone")
                .andExpression("distset").size().as("numberOfDistWards")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(unwindRequests,groupObject,projectToMatchModel,sortByWards,limitToOnlyFifty);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "citizen", String.class);

        return result.getMappedResults();
    }

    public List<String> query11(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").in(name));
        return mongoTemplate.findDistinct(query, "upvotedRequests.ward", "citizen", String.class);
    }
}
