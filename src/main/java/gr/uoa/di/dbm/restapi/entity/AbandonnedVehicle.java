package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="service_request")
@TypeAlias("abandonned_vehicles")
public class AbandonnedVehicle extends ServiceRequest {

	@Field("days_parked")
	private Double daysParked;

	@Field("license_plate")
	private String licensePlate;

	@Field("vehicle_color")
	private String vehicleColor;

	@Field("vehicle_model")
	private String vehicleModel;

	public AbandonnedVehicle() {
	}

	public Double getDaysParked() {
		return this.daysParked;
	}

	public void setDaysParked(Double daysParked) {
		this.daysParked = daysParked;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getVehicleColor() {
		return this.vehicleColor;
	}

	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

	public String getVehicleModel() {
		return this.vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

}