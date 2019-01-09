package gr.uoa.di.dbm.restapi.repo;

import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class ServiceRequestRepositoryImpl implements ServiceRequestRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ServiceRequestRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void query1(Date createDate, Date completionDate){
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("create_date").gte(createDate)
                .and("completion_date").lte(completionDate));
        GroupOperation countServiceRequestsByType = Aggregation.group("request_type").count().as("incidents");
        SortOperation sortByServiceRequestNoDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));
        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("incidents").as("incidents");

        Aggregation aggregation = newAggregation(matchDates, countServiceRequestsByType, sortByServiceRequestNoDesc, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "service_request", String.class);

        result.getRawResults().get("results");
    }
}
