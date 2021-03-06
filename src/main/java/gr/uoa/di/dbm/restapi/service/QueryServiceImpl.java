package gr.uoa.di.dbm.restapi.service;

import gr.uoa.di.dbm.restapi.entity.Citizen;
import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import gr.uoa.di.dbm.restapi.entity.UpvotedRequest;
import gr.uoa.di.dbm.restapi.repo.CitizenRepository;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryServiceImpl {

    private final ServiceRequestRepository serviceRequestRepository;
    private final CitizenRepository citizenRepository;

    @Autowired
    public QueryServiceImpl(ServiceRequestRepository serviceRequestRepository, CitizenRepository citizenRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.citizenRepository = citizenRepository;
    }

    public List<String> query1(Date startDate, Date endDate){
        return serviceRequestRepository.query1(startDate, endDate);
    }

    public List<String> query2(Date startDate, Date endDate, String requestType) {
        return serviceRequestRepository.query2(startDate, endDate, requestType);
    }

    public List<String> query3(Date startDate){
        return serviceRequestRepository.query3(startDate);
    }

    public List<String> query4(String requestType) {
        return serviceRequestRepository.query4(requestType);
    }

    public List<String> query5(Date startDate, Date endDate){
        return serviceRequestRepository.query5(startDate, endDate);
    }

    public List<String> query6(Date startDate, Double minLat, Double maxLat, Double minLon, Double maxLon) {
        return serviceRequestRepository.query6(startDate, minLat, maxLat, minLon, maxLon);
    }

    public List<String> query7(Date startDate) {
        return serviceRequestRepository.query7(startDate);
    }

    public List<String> query8() {
        return citizenRepository.query8();
    }

    public List<String> query9() {
        return citizenRepository.query9();
    }

    public List<String> query10() {
        return serviceRequestRepository.query10();
    }

    public List<String> query11(String name) {
        return citizenRepository.query11(name);
    }

    public ServiceRequest insertIncident(ServiceRequest serviceRequest){
        return serviceRequestRepository.save(serviceRequest);
    }

    public Map<String,String> upvoteIncident(String incidentId, String name){
        String resultMessage;
        Map<String,String> resultMap = new HashMap<>();
        Citizen citizen = citizenRepository.findByName(name);
        if(citizen != null) {
            Optional<ServiceRequest> optionalServiceRequest = serviceRequestRepository.findById(incidentId);
            if (optionalServiceRequest.isPresent()) {
                ServiceRequest serviceRequest = optionalServiceRequest.get();
                List<Citizen> upvotes = serviceRequest.getUpvotes() != null
                        ? serviceRequest.getUpvotes()
                        : new ArrayList();
                List<String> votersIds = upvotes.stream().
                        map(Citizen::getCitizenId)
                        .collect(Collectors.toList());

                if (!votersIds.contains(citizen.getCitizenId())) {
                    Citizen voter = citizen.getVoterFromCitizen();
                    upvotes.add(voter);
                    serviceRequest.setUpvotes(upvotes);

                    UpvotedRequest upvotedRequest = new UpvotedRequest(
                            serviceRequest.getServiceRequestId(),
                            serviceRequest.getLocation().getWard());
                    citizen.addUpvotedRequest(upvotedRequest);
                    citizen.increaseVotes();

                    citizenRepository.save(citizen);
                    serviceRequestRepository.save(serviceRequest);
                    resultMessage = "Incident with id "+incidentId+" has successfully been upvoted by user "+name;
                }
                else
                    resultMessage = "Error: User with name " + name + " has already upvote this incident!";
            }
            else
                resultMessage = "Error: No incident with id " + incidentId + " is stored in database!";
        }
        else
            resultMessage = "Error: No user with name "+ name +" is stored in database!";

        resultMap.put("result",resultMessage);
        return resultMap;
    }
}
