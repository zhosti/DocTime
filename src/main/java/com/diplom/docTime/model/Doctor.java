package com.diplom.docTime.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Doctor extends User{

		
	@ManyToOne(fetch=FetchType.EAGER)
	private DoctorType doctorType;

	@ManyToOne(fetch=FetchType.EAGER)
	private Hospital hospital;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="DOC_PATIENT")
	private Set<Patient> patients;

	public DoctorType getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(DoctorType doctorType) {
		this.doctorType = doctorType;
	}

	public Set<Patient> getPatients() {
		return patients;
	}

	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doctorType == null) ? 0 : doctorType.hashCode());
		result = prime * result + ((hospital == null) ? 0 : hospital.hashCode());
		result = prime * result + ((patients == null) ? 0 : patients.hashCode());
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
		Doctor other = (Doctor) obj;
		if (doctorType == null) {
			if (other.doctorType != null)
				return false;
		} else if (!doctorType.equals(other.doctorType))
			return false;
		if (hospital == null) {
			if (other.hospital != null)
				return false;
		} else if (!hospital.equals(other.hospital))
			return false;
		if (patients == null) {
			if (other.patients != null)
				return false;
		} else if (!patients.equals(other.patients))
			return false;
		return true;
	}
}
