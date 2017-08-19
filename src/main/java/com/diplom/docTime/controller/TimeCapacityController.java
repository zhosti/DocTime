package com.diplom.docTime.controller;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.DocTypeFacade;
import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.facade.TimeCapacityFacade;
import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.DoctorType;
import com.diplom.docTime.model.TimeCapacity;


@Named("timeCapacityController")
@RequestScoped
public class TimeCapacityController extends AbstractController<TimeCapacity> {

	private static final long serialVersionUID = 1L;

	public TimeCapacityController() {
		super(TimeCapacity.class);
	}

	@Inject
	private TimeCapacityFacade timeCapacityFacade;

	@Inject
	private DoctorFacade doctorFacade;

	private TimeCapacity capacity;
	
	private int count;
	/**
	 * keep all capacity by day of week
	 */
	private List<TimeCapacity> timeCapacityList = new ArrayList<TimeCapacity>();

	private int[] allServiceNom = new int[8];

	/**
	 * Save all entry from table timeCapacity
	 * 
	 * @return
	 */
	public String save1() {
		for (TimeCapacity timeCapacity : timeCapacityList) {
			timeCapacityFacade.edit(timeCapacity);
		}
		return "success";
	}


	public TimeCapacity services() {
		return timeCapacityList.get(0);
	}

	public TimeCapacity service(TimeCapacity capacity, Doctor service) {
			if (capacity.getDoctor().getDoctorType().getType()
					.equals(service.getDoctorType().getType())) {
				return capacity;
			}
		
		return null;
	}

	public List<TimeCapacity> gettimeCapacityList() {
		return timeCapacityList;
	}

	public void settimeCapacityList(List<TimeCapacity> timeCapacityList) {
		this.timeCapacityList = timeCapacityList;
	}

	public String getNameOfWeekDay(DayOfWeek dfW, String l) {
		Locale locale = new Locale(l);
		return dfW.getDisplayName(TextStyle.FULL, locale);
	}

	@PostConstruct
	@Override
	public void init() {

		super.setFacade(timeCapacityFacade);
		
		for (int i = 1; i < 8; i++) {
			capacity = new TimeCapacity();

			DayOfWeek dayOfWeek = DayOfWeek.of(i);

			Doctor doctor = doctorFacade.find(1);
	
			capacity.setDayOfWeek(dayOfWeek);
			capacity.setDoctor(doctor);
			timeCapacityList.add(capacity);
		}
		
	}

	public void updateAllServiceNom(TimeCapacity timeCap) {
		int sum = 0;
		//List<Service> services = timeCap.getServices();

//		for (Service ser : services) {
//			sum = sum + ser.getCount();
//
//		}
		int day = timeCap.getDayOfWeek().getValue();
		allServiceNom[day] = sum;

	}

	public int[] getAllServiceNom() {
		return allServiceNom;
	}

	public void setAllServiceNom(int[] allServiceNom) {
		this.allServiceNom = allServiceNom;
	}

	public TimeCapacity gettCapacity() {
		return capacity;
	}

	public void settCapacity(TimeCapacity capacity) {
		this.capacity = capacity;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}
}