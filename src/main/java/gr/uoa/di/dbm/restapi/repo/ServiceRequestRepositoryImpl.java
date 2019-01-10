package gr.uoa.di.dbm.restapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class ServiceRequestRepositoryImpl implements ServiceRequestRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ServiceRequestRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List query1(Date startDate, Date endDate){
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("create_date").gte(startDate).lte(endDate));
        GroupOperation countServiceRequestsByType = Aggregation.group("request_type").count().as("incidents");
        SortOperation sortByIncidentsDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));
        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("incidents").as("incidents");

        Aggregation aggregation = newAggregation(matchDates, countServiceRequestsByType, sortByIncidentsDesc, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "service_request", String.class);

        return result.getMappedResults();
    }

    public List query2(Date startDate, Date endDate, String requestType) {

        MatchOperation matchDates = Aggregation.match(Criteria
                .where("create_date").gte(startDate).lte(endDate));
        MatchOperation matchRequestType = Aggregation.match(Criteria
                .where("request_type").is(requestType));

        GroupOperation countServiceRequestsByDate = Aggregation.group("create_date").count().as("incidents");
        SortOperation sortByDateDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("create_date").as("createDate")
                .andExpression("incidents").as("incidents");

        Aggregation aggregation = newAggregation(matchDates, matchRequestType, countServiceRequestsByDate, sortByDateDesc, projectToMatchModel);
        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "service_request", String.class);

        return result.getMappedResults();
    }

    public List query4(String requestType) {
        MatchOperation matchRequestType = Aggregation.match(Criteria
                .where("request_type").is(requestType));
        GroupOperation countServiceRequestsByWard = Aggregation.group("location.ward","request_type").count().as("totalRequests");
        SortOperation sortByServiceRequests = Aggregation.sort(new Sort(Direction.ASC, "_id.ward","totalRequests"));

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id.ward").as("ward")
                .andExpression("totalRequests").as("totalRequests");

        Aggregation aggregation = newAggregation(matchRequestType, countServiceRequestsByWard, sortByServiceRequests, projectToMatchModel);
        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "service_request", String.class);

        return result.getMappedResults();
    }

    public List query5(Date startDate, Date endDate){

        //Check for null completion date because abandoned building don't have that field
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("create_date").gte(startDate).lte(endDate)
                .and("completion_date").exists(true));

        ArithmeticOperators.Subtract completionPeriod = ArithmeticOperators.Subtract.valueOf("completion_date").subtract("create_date");
        GroupOperation avgServiceRequestsByTypeOnCompletionPeriod = Aggregation.group("request_type").avg(completionPeriod).as("average_completion_period");

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("average_completion_period").as("averageCompletionPeriod");

        Aggregation aggregation = newAggregation(matchDates, avgServiceRequestsByTypeOnCompletionPeriod, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "service_request", String.class);

        return result.getMappedResults();
    }
}
