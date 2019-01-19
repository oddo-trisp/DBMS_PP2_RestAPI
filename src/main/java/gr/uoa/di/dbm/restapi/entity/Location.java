package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Field("latitude")
	private Double latitude;

	@Field("longitude")
	private Double longitude;

	@Field("address")
	private String address;

	@Field("communityArea")
	private String communityArea;

	@Field("coordinates")
	private Point coordinates;

	@Field("locationJson")
	private String locationJson;

	@Field("policeDistrict")
	private String policeDistrict;

	@Field("ssa")
	private String ssa;

	@Field("ward")
	private String ward;

	@Field("zipCode")
	private String zipCode;

	public Location() {
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCommunityArea() {
		return this.communityArea;
	}

	public void setCommunityArea(String communityArea) {
		this.communityArea = communityArea;
	}

	public Point getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}


	public String getLocationJson() {
		return this.locationJson;
	}

	public void setLocationJson(String locationJson) {
		this.locationJson = locationJson;
	}

	public String getPoliceDistrict() {
		return this.policeDistrict;
	}

	public void setPoliceDistrict(String policeDistrict) {
		this.policeDistrict = policeDistrict;
	}

	public String getSsa() {
		return this.ssa;
	}

	public void setSsa(String ssa) {
		this.ssa = ssa;
	}

	public String getWard() {
		return this.ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}