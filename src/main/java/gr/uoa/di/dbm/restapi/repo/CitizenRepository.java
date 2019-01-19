package gr.uoa.di.dbm.restapi.repo;

import gr.uoa.di.dbm.restapi.entity.Citizen;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CitizenRepository extends MongoRepository<Citizen, String> {
    List<Citizen> findTop50ByVotesNotNullOrderByVotesDesc();
    Citizen findByName(String name);
}