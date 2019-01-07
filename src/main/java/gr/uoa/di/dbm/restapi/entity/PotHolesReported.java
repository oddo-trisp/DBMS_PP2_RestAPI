package gr.uoa.di.dbm.restapi.entity;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="service_request")
@TypeAlias("pot_holes_reported")
public class PotHolesReported extends ServiceRequest {

	@Field("holes_num")
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