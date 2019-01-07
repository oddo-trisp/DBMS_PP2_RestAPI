package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="service_request")
@TypeAlias("rodent_baiting")
public class RodentBaiting extends ServiceRequest {

	@Field("baited_num")
	private Long baitedNum;

	@Field("garbage_num")
	private Long garbageNum;

	@Field("rats_num")
	private Long ratsNum;

	public RodentBaiting() {
	}

	public Long getBaitedNum() {
		return this.baitedNum;
	}

	public void setBaitedNum(Long baitedNum) {
		this.baitedNum = baitedNum;
	}

	public Long getGarbageNum() {
		return this.garbageNum;
	}

	public void setGarbageNum(Long garbageNum) {
		this.garbageNum = garbageNum;
	}

	public Long getRatsNum() {
		return this.ratsNum;
	}

	public void setRatsNum(Long ratsNum) {
		this.ratsNum = ratsNum;
	}

}