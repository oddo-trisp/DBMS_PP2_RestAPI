package gr.uoa.di.dbm.restapi.repo;

import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, String> {
}