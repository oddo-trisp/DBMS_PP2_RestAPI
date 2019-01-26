package gr.uoa.di.dbm.restapi.controller;

import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import gr.uoa.di.dbm.restapi.service.QueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class QueryController {

    private final QueryServiceImpl queryServiceImpl;
    private SimpleDateFormat dateFormat;

    @Autowired
    public QueryController(QueryServiceImpl queryServiceImpl) {
        this.queryServiceImpl = queryServiceImpl;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    @RequestMapping(value = "/query1", method = RequestMethod.GET)
    public List query1(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.query1(startDate, endDate);
    }

    @RequestMapping(value = "/query2", method = RequestMethod.GET)
    public List query2(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        String requestType = parameters.get("requestType");
        return queryServiceImpl.query2(startDate, endDate, requestType);
    }

    @RequestMapping(value = "/query4", method = RequestMethod.GET)
    public List query4(@RequestParam Map<String,String> parameters) {
        String requestType = parameters.get("requestType");
        return queryServiceImpl.query4(requestType);
    }

    @RequestMapping(value = "/query5", method = RequestMethod.GET)
    public List query5(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.query5(startDate, endDate);
    }

    @RequestMapping(value = "/query6", method = RequestMethod.GET)
    public List query6(@RequestParam Map<String,String> parameters) throws Exception {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Double minLat = Double.valueOf(parameters.get("minLat"));
        Double maxLat = Double.valueOf(parameters.get("maxLat"));
        Double minLon = Double.valueOf(parameters.get("minLon"));
        Double maxLon = Double.valueOf(parameters.get("maxLon"));
        return queryServiceImpl.query6(startDate, minLat, maxLat, minLon, maxLon);
    }


    @RequestMapping(value = "/query7", method = RequestMethod.GET)
    public List query7(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        return queryServiceImpl.query7(startDate);
    }

    @RequestMapping(value = "/query8", method = RequestMethod.GET)
    public List query8() {
        return queryServiceImpl.query8();
    }

    @RequestMapping(value = "/query9", method = RequestMethod.GET)
    public List query9() {
        return queryServiceImpl.query9();
    }

    @RequestMapping(value = "/query10", method = RequestMethod.GET)
    public List query10() {
        return queryServiceImpl.query10();
    }

    @RequestMapping(value = "/query11", method = RequestMethod.GET)
    public List query11(@RequestParam Map<String,String> parameters) {
        return queryServiceImpl.query11(parameters.get("name"));
    }


    @RequestMapping(value = "/insertIncident", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ServiceRequest insertIncident(@RequestBody ServiceRequest serviceRequest) {
        return queryServiceImpl.insertIncident(serviceRequest);
    }

    @RequestMapping(value = "/upvoteIncident", method = RequestMethod.POST)
    public String upvoteIncident(@RequestParam Map<String,String> parameters) {
        String incidentId = parameters.get("incidentId");
        String userName = parameters.get("userName");
        return queryServiceImpl.upvoteIncident(incidentId, userName);
    }

    /*@RequestMapping(value = "/bar", method = RequestMethod.GET)
    public List<ServiceRequest> bar(@RequestParam Map<String,String> parameters) throws ParseException {

        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.bar(startDate, endDate, parameters.get("type"));
    }*/
}
