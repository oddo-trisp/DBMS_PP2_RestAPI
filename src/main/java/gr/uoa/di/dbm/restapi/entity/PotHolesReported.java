package gr.uoa.di.dbm.restapi.entity;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="serviceRequest")
@TypeAlias("potHolesReported")
public class PotHolesReported extends ServiceRequest {

	@Field("holesNum")
	private Long holesNum;

	public PotHolesReported() {
	}

	public Long getHolesNum() {
		return this.holesNum;
	}

	public void setHolesNum(Long holesNum) {
		this.holesNum = holesNum;
	}

}