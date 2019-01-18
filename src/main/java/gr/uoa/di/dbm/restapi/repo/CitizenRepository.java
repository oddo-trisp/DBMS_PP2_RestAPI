package gr.uoa.di.dbm.restapi.repo;

import gr.uoa.di.dbm.restapi.entity.Citizen;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CitizenRepository extends MongoRepository<Citizen, String> {
}