package gr.uoa.di.dbm.restapi.entity;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="service_request")
@TypeAlias("trim_trees")
public class TrimTree extends ServiceRequest {

	@Field("trees_location")
	private String treesLocation;

	public TrimTree() {
	}

	public String getTreesLocation() {
		return this.treesLocation;
	}

	public void setTreesLocation(String treesLocation) {
		this.treesLocation = treesLocation;
	}

}