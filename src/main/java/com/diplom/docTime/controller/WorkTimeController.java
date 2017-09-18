package com.diplom.docTime.controller;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.CloseEvent;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.TimeSelectEvent;

import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.facade.WorkTimeFacade;
import com.diplom.docTime.model.BreakTime;
import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.WorkingTime;


/**
 * WorkTimeController
 * 
 */
@Named(value = "workTimeController")
@RequestScoped
public class WorkTimeController extends AbstractController<WorkingTime> {

	private static final long serialVersionUID = 1L;

	public WorkTimeController() {
		super(WorkingTime.class);
	}

	@Inject
	private WorkTimeFacade workingTimeFacade;

	@Inject
	private DoctorFacade doctorFacade;
	/**
	 * Schedule working time list
	 */
	private List<WorkingTime> workingTime = new ArrayList<WorkingTime>();

	/**
	 * Time schedule start time
	 */
	private Date startTime;

	/**
	 * Time schedule end time
	 */
	private Date endTime;

	private boolean showTime = false;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<WorkingTime> getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(List<WorkingTime> workingTime) {
		this.workingTime = workingTime;
	}

	public String getFormattedTimeStartTime() {
		return getFormattedTime(startTime, "HH:mm");
	}

	public String getFormattedTimeEndTime() {
		return getFormattedTime(endTime, "HH:mm");
	}

	public void showTime(ActionEvent ae) {
		showTime = true;
	}

	public boolean isShowTimeDialog() {
		if (showTime) {
			showTime = false;

			return true;
		}

		return false;
	}

	public void timeSelectListener(TimeSelectEvent timeSelectEvent) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Time select fired", "Selected time: "
						+ getFormattedTime(timeSelectEvent.getTime(), "HH:mm"));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void beforeShowListener(BeforeShowEvent beforeShowEvent) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Before show fired", "Component id: "
						+ beforeShowEvent.getComponent().getId());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void closeListener(CloseEvent closeEvent) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Close fired", "Component id: "
						+ closeEvent.getComponent().getId());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	private String getFormattedTime(Date time, String format) {
		if (time == null) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		return simpleDateFormat.format(time);
	}

	/**
	 * Save working time to database
	 */
	public String saveWorkingTime() {
		
		for (WorkingTime entity : workingTime) {
			workingTimeFacade.create(entity);
		}

		return "success";

	}

	public void addBreakTime(WorkingTime time) {

		BreakTime breakTime = new BreakTime();
		Calendar calendar = Calendar.getInstance();
		int hours = time.getBreakTime().get(time.getBreakTime().size() - 1).getEndDate().getHours();
		
		int minutes = time.getBreakTime().get(time.getBreakTime().size() - 1).getEndDate().getMinutes();
		minutes += 30;
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR, hours);
		calendar.set(Calendar.MINUTE,minutes);
		breakTime.setStartTime(calendar.getTime());

		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 00);
		breakTime.setEndDate(calendar.getTime());

		time.getBreakTime().add(breakTime);
	}

	/**
	 * Delete break time from working time table
	 * 
	 * @param day
	 * @param i
	 */
	public void deleteBreakTime(String day, int i) {

		for (WorkingTime time : workingTime) {
			if (time.getDayOfWeek().name().equals(day)) {
				time.getBreakTime().remove(i);
			}
		}
	}

	public String getNameOfWeekDay(DayOfWeek dfW, String l) {
		Locale locale = new Locale(l);
		return dfW.getDisplayName(TextStyle.FULL, locale);
	}

	/*
	 * Set working time data table for schedule
	 */
	@PostConstruct
	@Override
	public void init() {
		WorkingTime time = null;
		Calendar calendar = Calendar.getInstance();
		List<BreakTime> breakeTimeList = null;

		for (int i = 1; i < 8; i++) {
			DayOfWeek dayWeek = DayOfWeek.of(i);
			time = new WorkingTime();

			calendar.set(Calendar.AM_PM, Calendar.AM);
			calendar.set(Calendar.HOUR, 9);
			calendar.set(Calendar.MINUTE, 0);

			BreakTime breakTime = new BreakTime();

			breakTime.setStartTime(calendar.getTime());

			breakeTimeList = new ArrayList<BreakTime>();

			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE, 00);

			breakTime.setEndDate(calendar.getTime());
			breakeTimeList.add(breakTime);
			time.setDayOfWeek(dayWeek);
			time.setBreakTime(breakeTimeList);

			Doctor doctor = doctorFacade.find(262);

			time.setDoctor(doctor);
//			ConsulateNom cons = new ConsulateNom();
//			cons.setCode("Bul");
//			cons.setDescription("Bulgaria");
//			time.setConsulate(cons);
			workingTime.add(time);
		}

	}

	public WorkTimeFacade getWorkingTimeFacade() {
		return workingTimeFacade;
	}

	public void setWorkingTimeFacade(WorkTimeFacade workingTimeFacade) {
		this.workingTimeFacade = workingTimeFacade;
	}
	
	public void validate(BreakTime time) {
		startHour =	 time.getStartTime().getHours();
	}
	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	private int startHour = 0;
	
}