package com.diplom.docTime.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class City {
	
	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int Id;
	
	@NotNull
	private String Name;
	
	private String PostCode;
//
//	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
//	private Set<Hospital> hospitals;
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

//	public Set<Hospital> getHospitals() {
//		return hospitals;
//	}
//
//	public void setHospitals(Set<Hospital> hospitals) {
//		this.hospitals = hospitals;
//	}
//	
	
}
