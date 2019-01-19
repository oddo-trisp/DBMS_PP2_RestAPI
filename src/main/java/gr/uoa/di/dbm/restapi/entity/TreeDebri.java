package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="serviceRequest")
@TypeAlias("treeDebri")
public class TreeDebri extends ServiceRequest {

	@Field("debrisLocation")
	private String debrisLocation;

	public TreeDebri() {
	}

	public String getDebrisLocation() {
		return this.debrisLocation;
	}

	public void setDebrisLocation(String debrisLocation) {
		this.debrisLocation = debrisLocation;
	}

}