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

    @ResponseBody
    @RequestMapping(value = "/query1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  List<String> query1(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.query1(startDate, endDate);
    }

    @ResponseBody
    @RequestMapping(value = "/query2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query2(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        String requestType = parameters.get("requestType");
        return queryServiceImpl.query2(startDate, endDate, requestType);
    }

    @ResponseBody
    @RequestMapping(value = "/query3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query3(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        return queryServiceImpl.query3(startDate);
    }

    @ResponseBody
    @RequestMapping(value = "/query4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query4(@RequestParam Map<String,String> parameters) {
        String requestType = parameters.get("requestType");
        return queryServiceImpl.query4(requestType);
    }

    @ResponseBody
    @RequestMapping(value = "/query5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query5(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.query5(startDate, endDate);
    }

    @ResponseBody
    @RequestMapping(value = "/query6", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query6(@RequestParam Map<String,String> parameters) throws Exception {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Double minLat = Double.valueOf(parameters.get("minLat"));
        Double maxLat = Double.valueOf(parameters.get("maxLat"));
        Double minLon = Double.valueOf(parameters.get("minLon"));
        Double maxLon = Double.valueOf(parameters.get("maxLon"));
        return queryServiceImpl.query6(startDate, minLat, maxLat, minLon, maxLon);
    }

    @ResponseBody
    @RequestMapping(value = "/query7", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query7(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        return queryServiceImpl.query7(startDate);
    }

    @ResponseBody
    @RequestMapping(value = "/query8", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query8() {
        return queryServiceImpl.query8();
    }

    @ResponseBody
    @RequestMapping(value = "/query9", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query9() {
        return queryServiceImpl.query9();
    }

    @ResponseBody
    @RequestMapping(value = "/query10", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query10() {
        return queryServiceImpl.query10();
    }

    @ResponseBody
    @RequestMapping(value = "/query11", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> query11(@RequestParam Map<String,String> parameters) {
        return queryServiceImpl.query11(parameters.get("name"));
    }


    @ResponseBody
    @RequestMapping(value = "/insertIncident", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ServiceRequest insertIncident(@RequestBody ServiceRequest serviceRequest) {
        return queryServiceImpl.insertIncident(serviceRequest);
    }

    @ResponseBody
    @RequestMapping(value = "/upvoteIncident", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> upvoteIncident(@RequestParam Map<String,String> parameters) {
        String incidentId = parameters.get("incidentId");
        String userName = parameters.get("userName");
        return queryServiceImpl.upvoteIncident(incidentId, userName);
    }
}
