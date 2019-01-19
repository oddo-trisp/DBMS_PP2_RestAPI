package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="serviceRequest")
@TypeAlias("rodentBaiting")
public class RodentBaiting extends ServiceRequest {

	@Field("baitedNum")
	private Long baitedNum;

	@Field("garbageNum")
	private Long garbageNum;

	@Field("ratsNum")
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