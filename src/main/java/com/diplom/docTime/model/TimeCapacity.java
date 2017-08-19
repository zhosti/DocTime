package com.diplom.docTime.model;

import java.time.DayOfWeek;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class TimeCapacity {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private DayOfWeek dayOfWeek;

	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	private Doctor doctor;

	private int count;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


}
