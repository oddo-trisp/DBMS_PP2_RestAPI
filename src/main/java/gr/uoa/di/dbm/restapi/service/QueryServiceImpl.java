package gr.uoa.di.dbm.restapi.service;

import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QueryServiceImpl {

    private final ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public QueryServiceImpl(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    public List query1(Date startDate, Date endDate){
        return serviceRequestRepository.query1(startDate, endDate);
    }

    public List query4(Date startDate, Date endDate){
        return serviceRequestRepository.query4(startDate, endDate);
    }

    public List query6(Date startDate, Date endDate){
        return serviceRequestRepository.query6(startDate, endDate);
    }

    /*public List<ServiceRequest> bar(Date creationDate, Date completionDate, String type){
        return serviceRequestRepository.findByCreateDateGreaterThanEqualAndCompletionDateLessThanEqualAndRequestType(creationDate, completionDate, type);
    }*/
}
