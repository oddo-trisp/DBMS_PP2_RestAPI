package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="service_request")
@TypeAlias("garbage_cart")
public class GarbageCart extends ServiceRequest {

	@Field("carts_delivered")
	private Long cartsDelivered;

	public GarbageCart() {
	}

	public Long getCartsDelivered() {
		return this.cartsDelivered;
	}

	public void setCartsDelivered(Long cartsDelivered) {
		this.cartsDelivered = cartsDelivered;
	}

}