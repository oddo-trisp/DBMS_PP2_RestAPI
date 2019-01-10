package gr.uoa.di.dbm.restapi.repo;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepositoryCustom {
    List query1(Date startDate, Date endDate);
    List query5(Date startDate, Date endDate);
}
