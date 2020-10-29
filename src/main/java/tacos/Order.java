package tacos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;

public class Order {
	
	private Long id;
	private Date placedAt;
	
	@NotBlank(message="Name is Required")
	private String deliveryName;
	@NotBlank(message="Street is Required")
	private String deliveryStreet;
	@NotBlank(message="City is Required")
	private String deliveryCity;
	@NotBlank(message="State is Required")
	private String deliveryState;
	@NotBlank(message="Zip is Required")
	private String deliveryZip;
	@CreditCardNumber(message="Not a valid Credit Card Number")
	private String ccNumber;
	@Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
	private String ccExpiration;
	@Digits(integer=3, fraction=0, message="Invalid CVV")
	private String ccCVV;
	
	
	private List<Taco> tacos = new ArrayList<>();
	
	public void addDesign(Taco design) {
		this.tacos.add(design);
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getPlacedAt() {
		return placedAt;
	}
	public void setPlacedAt(Date placedAt) {
		this.placedAt = placedAt;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliveryStreet() {
		return deliveryStreet;
	}
	public void setDeliveryStreet(String deliveryStreet) {
		this.deliveryStreet = deliveryStreet;
	}
	public String getDeliveryCity() {
		return deliveryCity;
	}
	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}
	public String getDeliveryState() {
		return deliveryState;
	}
	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}
	public String getDeliveryZip() {
		return deliveryZip;
	}
	public void setDeliveryZip(String deliveryZip) {
		this.deliveryZip = deliveryZip;
	}
	public String getCcNumber() {
		return ccNumber;
	}
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
	public String getCcExpiration() {
		return ccExpiration;
	}
	public void setCcExpiration(String ccExpiration) {
		this.ccExpiration = ccExpiration;
	}
	public String getCcCVV() {
		return ccCVV;
	}
	public void setCcCVV(String ccCVV) {
		this.ccCVV = ccCVV;
	}
	
}
