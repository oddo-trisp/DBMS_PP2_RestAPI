package gr.uoa.di.dbm.restapi.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_class")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "graffity_removal", value = GraffityRemoval.class),
})*/
@Document(collection="serviceRequest")
@TypeAlias("serviceRequest")
public class ServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String serviceRequestId;

	@Field("completion_date")
	private Date completionDate;

	@Field("create_date")
	private Date createDate;

	@Field("current_activity")
	private String currentActivity;

	@Field("most_recent_action")
	private String mostRecentAction;

	@Field("request_type")
	private String requestType;

	@Field("service_request_no")
	private String serviceRequestNo;

	@Field("status")
	private String status;

	@Field("location")
	private Location location;

	@Field("upvotes")
	private List<String> upvotes;

	public ServiceRequest() {
	}

	public String getServiceRequestId() {
		return this.serviceRequestId;
	}

	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public Date getCompletionDate() {
		return this.completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCurrentActivity() {
		return this.currentActivity;
	}

	public void setCurrentActivity(String currentActivity) {
		this.currentActivity = currentActivity;
	}

	public String getMostRecentAction() {
		return this.mostRecentAction;
	}

	public void setMostRecentAction(String mostRecentAction) {
		this.mostRecentAction = mostRecentAction;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getServiceRequestNo() {
		return this.serviceRequestNo;
	}

	public void setServiceRequestNo(String serviceRequestNo) {
		this.serviceRequestNo = serviceRequestNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<String> getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(List<String> upvotes) {
		this.upvotes = upvotes;
	}
}