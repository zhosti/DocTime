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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + ((PostCode == null) ? 0 : PostCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (Id != other.Id)
			return false;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (PostCode == null) {
			if (other.PostCode != null)
				return false;
		} else if (!PostCode.equals(other.PostCode))
			return false;
		return true;
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
