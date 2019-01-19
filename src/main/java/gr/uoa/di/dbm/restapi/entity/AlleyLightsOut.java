package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="serviceRequest")
@TypeAlias("alleyLightsOut")
public class AlleyLightsOut extends ServiceRequest{
}
