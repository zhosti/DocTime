package com.diplom.docTime.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class WorkingTime {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private DayOfWeek dayOfWeek;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Doctor doctor;
	
	@NotNull
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<BreakTime> breakTime = new ArrayList<>();

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

	public List<BreakTime> getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(List<BreakTime> breakTime) {
		this.breakTime = breakTime;
	}


}
