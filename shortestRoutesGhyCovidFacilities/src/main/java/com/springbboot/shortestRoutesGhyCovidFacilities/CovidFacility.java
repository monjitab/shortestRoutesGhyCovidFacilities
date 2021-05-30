package com.springbboot.shortestRoutesGhyCovidFacilities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*Entity for JPA Repository*/

@Entity
public class CovidFacility {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String type;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	
	
	public void setId(int id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public int getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	
}
