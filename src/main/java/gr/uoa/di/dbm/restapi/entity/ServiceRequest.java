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

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_class")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "abandonnedBuilding", value = AbandonnedBuilding.class),
		@JsonSubTypes.Type(name = "abandonnedVehicle", value = AbandonnedVehicle.class),
		@JsonSubTypes.Type(name = "alleyLightsOut", value = AlleyLightsOut.class),
		@JsonSubTypes.Type(name = "garbageCart", value = GarbageCart.class),
		@JsonSubTypes.Type(name = "graffityRemoval", value = GraffityRemoval.class),
		@JsonSubTypes.Type(name = "lightsOutAll", value = LightsOutAll.class),
		@JsonSubTypes.Type(name = "lightsOutOne", value = LightsOutOne.class),
		@JsonSubTypes.Type(name = "potHolesReported", value = PotHolesReported.class),
		@JsonSubTypes.Type(name = "rodentBaiting", value = RodentBaiting.class),
		@JsonSubTypes.Type(name = "sanitationCodeComplaint", value = SanitationCodeComplaint.class),
		@JsonSubTypes.Type(name = "treeDebri", value = TreeDebri.class),
		@JsonSubTypes.Type(name = "trimTree", value = TrimTree.class)
})
@Document(collection="serviceRequest")
@TypeAlias("serviceRequest")
public abstract class ServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String serviceRequestId;

	@Field("completionDate")
	private Date completionDate;

	@Field("createDate")
	private Date createDate;

	@Field("currentActivity")
	private String currentActivity;

	@Field("mostRecentAction")
	private String mostRecentAction;

	@Field("requestType")
	private String requestType;

	@Field("serviceRequestNo")
	private String serviceRequestNo;

	@Field("status")
	private String status;

	@Field("location")
	private Location location;

	@Field("upvotes")
	private List<Citizen> upvotes;

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

	public List<Citizen> getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(List<Citizen> upvotes) {
		this.upvotes = upvotes;
	}
}