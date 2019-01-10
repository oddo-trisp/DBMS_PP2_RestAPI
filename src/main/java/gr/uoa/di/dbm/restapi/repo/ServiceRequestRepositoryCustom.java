package gr.uoa.di.dbm.restapi.repo;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepositoryCustom {
    List query1(Date creationDate, Date completionDate);
}
