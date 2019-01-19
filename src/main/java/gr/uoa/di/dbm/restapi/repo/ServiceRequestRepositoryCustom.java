package gr.uoa.di.dbm.restapi.repo;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepositoryCustom {
    List query1(Date startDate, Date endDate);
    List query2(Date startDate, Date endDate, String requestType);
    List query4(String requestType);
    List query5(Date startDate, Date endDate);
    List query6(Date startDate, Double minLat, Double maxLat, Double minLon, Double maxLon);
    List query7(Date startDate);
}
