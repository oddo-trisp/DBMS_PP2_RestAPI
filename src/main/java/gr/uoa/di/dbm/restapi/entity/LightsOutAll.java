package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="service_request")
@TypeAlias("lights_out_all")
public class LightsOutAll extends ServiceRequest{
}
