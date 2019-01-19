package gr.uoa.di.dbm.restapi.service;

import gr.uoa.di.dbm.restapi.repo.CitizenRepository;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public List query7(Date startDate) { return serviceRequestRepository.query7(startDate); }

    public List query8() { return citizenRepository.findTop50ByVotesNotNullOrderByVotesDesc(); }

    /*public List<ServiceRequest> bar(Date creationDate, Date completionDate, String type){
        return serviceRequestRepository.findByCreateDateGreaterThanEqualAndCompletionDateLessThanEqualAndRequestType(creationDate, completionDate, type);
    }*/
}
