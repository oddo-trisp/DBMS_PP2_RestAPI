package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="service_request")
@TypeAlias("abandonned_buildings")
public class AbandonnedBuilding extends ServiceRequest {

	@Field("building_dangerous")
	private Boolean buildingDangerous;

	@Field("building_entrance")
	private String buildingEntrance;

	@Field("building_fire")
	private Boolean buildingFire;

	@Field("building_location_on_the_lot")
	private String buildingLocationOnTheLot;

	@Field("building_open")
	private String buildingOpen;

	@Field("building_usage")
	private Boolean buildingUsage;

	@Field("building_vacant")
	private String buildingVacant;

	public AbandonnedBuilding() {
	}

	public Boolean getBuildingDangerous() {
		return this.buildingDangerous;
	}

	public void setBuildingDangerous(Boolean buildingDangerous) {
		this.buildingDangerous = buildingDangerous;
	}

	public String getBuildingEntrance() {
		return this.buildingEntrance;
	}

	public void setBuildingEntrance(String buildingEntrance) {
		this.buildingEntrance = buildingEntrance;
	}

	public Boolean getBuildingFire() {
		return this.buildingFire;
	}

	public void setBuildingFire(Boolean buildingFire) {
		this.buildingFire = buildingFire;
	}

	public String getBuildingLocationOnTheLot() {
		return this.buildingLocationOnTheLot;
	}

	public void setBuildingLocationOnTheLot(String buildingLocationOnTheLot) {
		this.buildingLocationOnTheLot = buildingLocationOnTheLot;
	}

	public String getBuildingOpen() {
		return this.buildingOpen;
	}

	public void setBuildingOpen(String buildingOpen) {
		this.buildingOpen = buildingOpen;
	}

	public Boolean getBuildingUsage() {
		return this.buildingUsage;
	}

	public void setBuildingUsage(Boolean buildingUsage) {
		this.buildingUsage = buildingUsage;
	}

	public String getBuildingVacant() {
		return this.buildingVacant;
	}

	public void setBuildingVacant(String buildingVacant) {
		this.buildingVacant = buildingVacant;
	}

}