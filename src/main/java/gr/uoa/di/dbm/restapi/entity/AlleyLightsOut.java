package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="service_request")
@TypeAlias("alley_lights_out")
public class AlleyLightsOut extends ServiceRequest{
}
