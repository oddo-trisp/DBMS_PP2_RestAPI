package gr.uoa.di.dbm.restapi.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.sql.Timestamp;

@Document(collection="service_request")
@TypeAlias("service_request")
public abstract class ServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId serviceRequestId;

	@Field("completion_date")
	private Timestamp completionDate;

	@Field("create_date")
	private Timestamp createDate;

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

	public ServiceRequest() {
	}

	public ObjectId getServiceRequestId() {
		return this.serviceRequestId;
	}

	public void setServiceRequestId(ObjectId serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public Timestamp getCompletionDate() {
		return this.completionDate;
	}

	public void setCompletionDate(Timestamp completionDate) {
		this.completionDate = completionDate;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
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

}