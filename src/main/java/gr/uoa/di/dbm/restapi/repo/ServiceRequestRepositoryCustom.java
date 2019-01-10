package gr.uoa.di.dbm.restapi.repo;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepositoryCustom {
    List query1(Date startDate, Date endDate);
    List query4(Date startDate, Date endDate);
    List query6(Date startDate, Date endDate);
}
