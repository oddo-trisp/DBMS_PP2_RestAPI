package gr.uoa.di.dbm.restapi.repo;

import gr.uoa.di.dbm.restapi.entity.Citizen;
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

    //TODO return whole citizen
    public List query9(){
        UnwindOperation unwindRequests = Aggregation.unwind("upvotedRequests");
        GroupOperation groupObject = Aggregation.group("_id","name")
                .addToSet("upvotedRequests.ward").as("distset");
        SortOperation sortByWards = Aggregation.sort(new Sort(Sort.Direction.DESC, "numberOfDistWards"));
        LimitOperation limitToOnlyFifty = Aggregation.limit(50);

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("distset").size().as("numberOfDistWards");

        Aggregation aggregation = newAggregation(unwindRequests,groupObject,projectToMatchModel,sortByWards,limitToOnlyFifty);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "citizen", String.class);

        return result.getMappedResults();
    }

    public List query11(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").in(name));
        return mongoTemplate.findDistinct(query, "upvotedRequests.ward", "citizen", String.class);
    }
}
