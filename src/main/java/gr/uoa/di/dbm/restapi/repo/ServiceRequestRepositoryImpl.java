package gr.uoa.di.dbm.restapi.repo;

import org.bson.BsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
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

    public List<String> query1(Date startDate, Date endDate){
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("createDate").gte(startDate).lte(endDate));
        GroupOperation countServiceRequestsByType = Aggregation.group("requestType").count().as("incidents");
        SortOperation sortByIncidentsDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));
        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("incidents").as("incidents")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(matchDates, countServiceRequestsByType, sortByIncidentsDesc, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List<String> query2(Date startDate, Date endDate, String requestType) {

        MatchOperation matchDatesAndRequestType = Aggregation.match(Criteria
                .where("createDate").gte(startDate).lte(endDate)
                .and("requestType").is(requestType));

        GroupOperation countServiceRequestsByDate = Aggregation.group("createDate").count().as("incidents");
        SortOperation sortByDateDesc = Aggregation.sort(new Sort(Direction.ASC, "_id"));

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").dateAsFormattedString("%Y-%m-%d").as("createDate")
                .andExpression("incidents").as("incidents")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(matchDatesAndRequestType, countServiceRequestsByDate, sortByDateDesc, projectToMatchModel);
        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List<String> query4(String requestType) {
        MatchOperation matchRequestType = Aggregation.match(Criteria
                .where("requestType").is(requestType)
                .and("location.ward").exists(true).ne(""));
        GroupOperation countServiceRequestsByWard = Aggregation.group("location.ward","requestType").count().as("totalRequests");
        SortOperation sortByServiceRequests = Aggregation.sort(new Sort(Direction.ASC, "totalRequests"));
        LimitOperation limitTop3 = Aggregation.limit(3);

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id.ward").as("ward")
                .andExpression("_id.requestType").as("requestType")
                .andExpression("totalRequests").as("totalRequests");

        Aggregation aggregation = newAggregation(matchRequestType, countServiceRequestsByWard, sortByServiceRequests, limitTop3, projectToMatchModel);
        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List<String> query5(Date startDate, Date endDate){

        //Check for null completion date because abandoned building don't have that field
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("createDate").gte(startDate).lte(endDate)
                .and("completionDate").exists(true));

        ArithmeticOperators.Subtract completionPeriod = ArithmeticOperators.Subtract.valueOf("completionDate").subtract("createDate");
        GroupOperation avgServiceRequestsByTypeOnCompletionPeriod = Aggregation.group("requestType").avg(completionPeriod).as("averageCompletionPeriod");

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("averageCompletionPeriod").as("averageCompletionPeriod")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(matchDates, avgServiceRequestsByTypeOnCompletionPeriod, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List<String> query6(Date startDate, Double minLat, Double maxLat, Double minLon, Double maxLon) {
        Box boundingBox = new Box(new Point(minLon,maxLat), new Point(maxLon, minLat));

        MatchOperation matchDateAndLocation = Aggregation.match(Criteria
                .where("createDate").is(startDate)
                .and("location.longitudeLatitude").within(boundingBox));

        GroupOperation countServiceRequestsByType = Aggregation.group("requestType").count().as("incidents");
        SortOperation sortByIncidentsDesc = Aggregation.sort(new Sort(Direction.DESC, "incidents"));
        LimitOperation limitOperation = Aggregation.limit(1);

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("requestType")
                .andExpression("incidents").as("incidents")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(matchDateAndLocation, countServiceRequestsByType, sortByIncidentsDesc, limitOperation, projectToMatchModel);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    public List<String> query7(Date startDate){

        //Check for null completion date because abandoned building don't have that field
        MatchOperation matchDates = Aggregation.match(Criteria
                .where("createDate").is(startDate)
                .and("upvotes").type(BsonType.ARRAY.getValue()));

        SortOperation sortByUpvotes = Aggregation.sort(new Sort(Direction.DESC, "numberOfUpvotes"));

        LimitOperation limitToOnlyFifty = Aggregation.limit(50);


        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("serviceRequestId")
                .andExpression("serviceRequestNo").as("serviceRequestNo")
                .andExpression("requestType").as("requestType")
                .andExpression("upvotes").size().as("numberOfUpvotes")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(matchDates,projectToMatchModel,sortByUpvotes,limitToOnlyFifty);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }

    //TODO recheck!!!!!
    public List<String> query10(){
        //Check for null completion date because abandoned building don't have that field
        MatchOperation matchType = Aggregation.match(Criteria
                .where("upvotes").type(BsonType.ARRAY.getValue()));

        UnwindOperation unwindUpVotes = Aggregation.unwind("upvotes");

        GroupOperation groupUpVotes = Aggregation.group("_id","serviceRequestNo").addToSet("upvotes.phone").as("distPhones")
                .addToSet("upvotes.name").as("distVotes");

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id._id").as("serviceRequestId")
                .andExpression("_id.serviceRequestNo").as("serviceRequestNo")
                .andExpression("distPhones").size().as("numberOfPhones")
                .andExpression("distVotes").size().as("numberOfUpvotes");
        ProjectionOperation projectionOperation = Aggregation.project("serviceRequestId","serviceRequestNo","numberOfPhones", "numberOfUpvotes")
                .andExpression("numberOfPhones!=numberOfUpvotes").as("isLess");

        MatchOperation matchNumber = Aggregation.match(Criteria.where("isLess").is(true));

        ProjectionOperation finalProjection = Aggregation.project("serviceRequestId","serviceRequestNo").andExclude("_id");


        Aggregation aggregation = newAggregation(matchType,unwindUpVotes,groupUpVotes,projectToMatchModel,projectionOperation,matchNumber, finalProjection);

        AggregationResults<String> result = mongoTemplate.aggregate(aggregation, "serviceRequest", String.class);

        return result.getMappedResults();
    }
}
