package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="service_request")
@TypeAlias("graffity_removal")
public class GraffityRemoval extends ServiceRequest {

	@Field("graffity_location")
	private String graffityLocation;

	@Field("surface")
	private String surface;

	public GraffityRemoval() {
	}

	public String getGraffityLocation() {
		return this.graffityLocation;
	}

	public void setGraffityLocation(String graffityLocation) {
		this.graffityLocation = graffityLocation;
	}

	public String getSurface() {
		return this.surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

}