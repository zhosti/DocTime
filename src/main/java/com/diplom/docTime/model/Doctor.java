package com.diplom.docTime.model;

import java.util.Set;

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
public class Doctor {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String firstName;
	
	private String middleName;
	
	@NotNull
	private String lastName;
		
	@ManyToOne(fetch=FetchType.EAGER)
	private DoctorType doctorType;

	@ManyToOne(fetch=FetchType.EAGER)
	private Hospital hospital;
	
	@ManyToMany
	@JoinTable(name="DOC_PATIENT")
	private Set<Patient> patients;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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
	
}
