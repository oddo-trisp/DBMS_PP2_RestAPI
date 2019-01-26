package gr.uoa.di.dbm.restapi.service;

import gr.uoa.di.dbm.restapi.entity.*;
import gr.uoa.di.dbm.restapi.repo.CitizenRepository;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryServiceImpl {

    private final ServiceRequestRepository serviceRequestRepository;
    private final CitizenRepository citizenRepository;

    @Autowired
    public QueryServiceImpl(ServiceRequestRepository serviceRequestRepository, CitizenRepository citizenRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.citizenRepository = citizenRepository;
    }

    public List query1(Date startDate, Date endDate){
        return serviceRequestRepository.query1(startDate, endDate);
    }

    public List query2(Date startDate, Date endDate, String requestType) { return serviceRequestRepository.query2(startDate, endDate, requestType); }

    public List query4(String requestType) { return serviceRequestRepository.query4(requestType); }

    public List query5(Date startDate, Date endDate){
        return serviceRequestRepository.query5(startDate, endDate);
    }

    public List query6(Date startDate, Double minLat, Double maxLat, Double minLon, Double maxLon) {
        return serviceRequestRepository.query6(startDate, minLat, maxLat, minLon, maxLon);
    }

    public List query7(Date startDate) { return serviceRequestRepository.query7(startDate); }

    public List query8() { return citizenRepository.findTop50ByVotesNotNullOrderByVotesDesc(); }

    public List query9() { return citizenRepository.query9(); }

    public List query10() { return serviceRequestRepository.query10(); }

    public List query11(String name) { return citizenRepository.query11(name); }

    public ServiceRequest insertIncident(ServiceRequest serviceRequest){
        return serviceRequestRepository.save(serviceRequest);
    }

    public String upvoteIncident(String incidentId, String name){
        String result;
        Citizen citizen = citizenRepository.findByName(name);
        if(citizen != null) {
            Optional<ServiceRequest> optionalServiceRequest = serviceRequestRepository.findById(incidentId);
            if (optionalServiceRequest.isPresent()) {
                ServiceRequest serviceRequest = optionalServiceRequest.get();
                List upvotes = serviceRequest.getUpvotes() != null
                        ? serviceRequest.getUpvotes()
                        : new ArrayList();
                if (!upvotes.contains(citizen.getCitizenId())) {
                    upvotes.add(citizen.getCitizenId());
                    serviceRequest.setUpvotes(upvotes);
                    citizen.increaseVotes();
                    citizenRepository.save(citizen);
                    serviceRequestRepository.save(serviceRequest);
                    result = "Incident with id "+incidentId+" has successfully been upvoted by user "+name;
                }
                else
                    result = "Error: User with name " + name + " has already upvote this incident!";
            }
            else
                result = "Error: No incident with id " + incidentId + " is stored in database!";
        }
        else
            result = "Error: No user with name "+ name +" is stored in database!";
        return result;
    }

    /*public List<ServiceRequest> bar(Date creationDate, Date completionDate, String type){
        return serviceRequestRepository.findByCreateDateGreaterThanEqualAndCompletionDateLessThanEqualAndRequestType(creationDate, completionDate, type);
    }*/
}
