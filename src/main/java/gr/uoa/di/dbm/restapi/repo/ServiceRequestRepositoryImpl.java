package gr.uoa.di.dbm.restapi.repo;

import org.bson.BsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
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
                .where("createDate").gte(startDate).lte(endDate));
        GroupOperation countServiceRequestsByType = Aggregation.group("requestType").count().as("incidents");
        SortOperation sortByIncidentsDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));
        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("incidents").as("incidents");

        Aggregation aggregation = newAggregation(matchDates, countServiceRequestsByType, sortByIncidentsDesc, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    //TODO Date format on results
    public List query2(Date startDate, Date endDate, String requestType) {

        MatchOperation matchDatesAndRequestType = Aggregation.match(Criteria
                .where("createDate").gte(startDate).lte(endDate)
                .and("requestType").is(requestType));

        GroupOperation countServiceRequestsByDate = Aggregation.group("createDate").count().as("incidents");
        SortOperation sortByDateDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("createDate").as("createDate")
                .andExpression("incidents").as("incidents");

        Aggregation aggregation = newAggregation(matchDatesAndRequestType, countServiceRequestsByDate, sortByDateDesc, projectToMatchModel);
        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List query4(String requestType) {
        MatchOperation matchRequestType = Aggregation.match(Criteria
                .where("requestType").is(requestType));
        GroupOperation countServiceRequestsByWard = Aggregation.group("location.ward","requestType").count().as("totalRequests");
        SortOperation sortByServiceRequests = Aggregation.sort(new Sort(Direction.ASC, "_id.ward","totalRequests"));

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id.ward").as("ward")
                .andExpression("totalRequests").as("totalRequests");

        Aggregation aggregation = newAggregation(matchRequestType, countServiceRequestsByWard, sortByServiceRequests, projectToMatchModel);
        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List query5(Date startDate, Date endDate){

        //Check for null completion date because abandoned building don't have that field
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("createDate").gte(startDate).lte(endDate)
                .and("completionDate").exists(true));

        ArithmeticOperators.Subtract completionPeriod = ArithmeticOperators.Subtract.valueOf("completionDate").subtract("createDate");
        GroupOperation avgServiceRequestsByTypeOnCompletionPeriod = Aggregation.group("requestType").avg(completionPeriod).as("averageCompletionPeriod");

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("averageCompletionPeriod").as("averageCompletionPeriod");

        Aggregation aggregation = newAggregation(matchDates, avgServiceRequestsByTypeOnCompletionPeriod, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    //TODO Bring all fields as result
    public List query7(Date startDate){

        //Check for null completion date because abandoned building don't have that field
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("createDate").is(startDate)
                .and("upvotes").type(BsonType.ARRAY.getValue()));

        SortOperation sortByUpvotes = Aggregation.sort(new Sort(Direction.DESC, "numberOfUpvotes"));

        LimitOperation limitToOnlyFifty = Aggregation.limit(50);

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("upvotes").size().as("numberOfUpvotes");

        Aggregation aggregation = newAggregation(matchDates,projectToMatchModel,sortByUpvotes,limitToOnlyFifty);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }
}
