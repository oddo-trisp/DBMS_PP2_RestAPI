package gr.uoa.di.dbm.restapi.controller;

import gr.uoa.di.dbm.restapi.service.ServiceRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ServiceRequestController {
    private final ServiceRequestServiceImpl serviceRequestServiceImpl;

    @Autowired
    public ServiceRequestController(ServiceRequestServiceImpl serviceRequestServiceImpl) {
        this.serviceRequestServiceImpl = serviceRequestServiceImpl;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void modifyPetById(){
        serviceRequestServiceImpl.parseData();
    }
}
