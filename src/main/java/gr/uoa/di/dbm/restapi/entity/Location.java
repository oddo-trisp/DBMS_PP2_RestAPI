package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Field("longitudeLatitude")
	private GeoJsonPoint longitudeLatitude;

	@Field("address")
	private String address;

	@Field("communityArea")
	private String communityArea;

	@Field("coordinates")
	private GeoJsonPoint coordinates;

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

	public GeoJsonPoint getLongitudeLatitude() {
		return longitudeLatitude;
	}

	public void setLongitudeLatitude(GeoJsonPoint longitudeLatitude) {
		this.longitudeLatitude = longitudeLatitude;
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

	public GeoJsonPoint getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(GeoJsonPoint coordinates) {
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