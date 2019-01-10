package gr.uoa.di.dbm.restapi.controller;

import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import gr.uoa.di.dbm.restapi.service.QueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List query4(@RequestParam Map<String,String> parameters) throws ParseException {
        String requestType = parameters.get("requestType");
        return queryServiceImpl.query4(requestType);
    }

    @RequestMapping(value = "/query5", method = RequestMethod.GET)
    public List query5(@RequestParam Map<String,String> parameters) throws ParseException {
        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.query5(startDate, endDate);
    }

    /*@RequestMapping(value = "/bar", method = RequestMethod.GET)
    public List<ServiceRequest> bar(@RequestParam Map<String,String> parameters) throws ParseException {

        Date startDate = dateFormat.parse(parameters.get("startDate"));
        Date endDate = dateFormat.parse(parameters.get("endDate"));
        return queryServiceImpl.bar(startDate, endDate, parameters.get("type"));
    }*/
}
