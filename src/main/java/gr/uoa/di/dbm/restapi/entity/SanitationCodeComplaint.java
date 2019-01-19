package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="serviceRequest")
@TypeAlias("sanitationCodeComplaint")
public class SanitationCodeComplaint extends ServiceRequest {

	@Field("natureViolation")
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