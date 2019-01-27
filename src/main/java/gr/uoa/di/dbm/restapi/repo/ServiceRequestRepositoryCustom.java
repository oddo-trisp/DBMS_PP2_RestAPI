package gr.uoa.di.dbm.restapi.repo;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepositoryCustom {
    List<String> query1(Date startDate, Date endDate);
    List<String> query2(Date startDate, Date endDate, String requestType);
    List<String> query4(String requestType);
    List<String> query5(Date startDate, Date endDate);
    List<String> query6(Date startDate, Double minLat, Double maxLat, Double minLon, Double maxLon);
    List<String> query7(Date startDate);
    List<String> query10();
}
