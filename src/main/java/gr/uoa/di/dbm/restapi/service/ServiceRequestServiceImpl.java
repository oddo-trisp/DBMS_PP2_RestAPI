package gr.uoa.di.dbm.restapi.service;

import gr.uoa.di.dbm.restapi.ParserCSV;
import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRequestServiceImpl {
    final ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    public void parseData(){
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        serviceRequests = new ParserCSV().parseData(serviceRequests);
        serviceRequestRepository.saveAll(serviceRequests);
    }
}
