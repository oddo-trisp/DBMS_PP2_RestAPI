package gr.uoa.di.dbm.restapi.repo;

import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, String>, ServiceRequestRepositoryCustom {
    //List<ServiceRequest> findByCreateDateGreaterThanEqualAndCompletionDateLessThanEqualAndRequestType(Date from, Date to, String type);
    //public void countByServiceRequestNoAndCreateDateGreaterThanEqualAndCompletionDateLessThanEqualAndRequestType(Date from, Date to, String type);
}