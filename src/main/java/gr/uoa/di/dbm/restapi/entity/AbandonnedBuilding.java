package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="serviceRequest")
@TypeAlias("abandonnedBuilding")
public class AbandonnedBuilding extends ServiceRequest {

	@Field("buildingDangerous")
	private Boolean buildingDangerous;

	@Field("buildingEntrance")
	private String buildingEntrance;

	@Field("buildingFire")
	private Boolean buildingFire;

	@Field("buildingLocationOnTheLot")
	private String buildingLocationOnTheLot;

	@Field("buildingOpen")
	private String buildingOpen;

	@Field("buildingUsage")
	private Boolean buildingUsage;

	@Field("buildingVacant")
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