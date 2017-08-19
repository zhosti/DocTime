package com.diplom.docTime.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Hospital {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int Id;
	
	@NotNull
	private String Name;
	
	@NotNull
	private String Address;
	
	@NotNull
	private String PhoneNumber;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private City City;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public City getCity() {
		return City;
	}

	public void setCity(City city) {
		City = city;
	}
	
}
