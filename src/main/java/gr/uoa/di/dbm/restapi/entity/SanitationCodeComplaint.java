package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="service_request")
@TypeAlias("sanitation_code_complaints")
public class SanitationCodeComplaint extends ServiceRequest {

	@Field("nature_violation")
	private String natureViolation;

	public SanitationCodeComplaint() {
	}

	public String getNatureViolation() {
		return this.natureViolation;
	}

	public void setNatureViolation(String natureViolation) {
		this.natureViolation = natureViolation;
	}

}