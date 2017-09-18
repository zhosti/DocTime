package com.diplom.docTime.controller;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.facade.PatientFacade;
import com.diplom.docTime.facade.TimeSlotFacade;
import com.diplom.docTime.facade.UserFacade;
import com.diplom.docTime.model.BreakTime;
import com.diplom.docTime.model.City;
import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.DoctorType;
import com.diplom.docTime.model.Hospital;
import com.diplom.docTime.model.Patient;
import com.diplom.docTime.model.TimeCapacity;
import com.diplom.docTime.model.TimeSlot;
import com.diplom.docTime.model.WorkingTime;


@Named(value="timeSlotController")
@SessionScoped
public class TimeSlotController extends AbstractController<TimeSlot> implements Serializable{

	private static final long serialVersionUID = -682385338296072513L;

	
	private static final Integer APPLY_DAYS_COUNT = 2;
	private static final String AGENDA_DAY_VIEW = "agendaDay";
	private static final String MONTH_VIEW = "month";
	private static final String DEFAULT_EVENT = "event-color-default";
	private static final String RESERVED_EVENT = "event-color-fail";
	private static final String FREE_EVENT = "event-color-success";
	
	private ScheduleModel eventModel;
	private DefaultScheduleEvent event = new DefaultScheduleEvent();

	private List<Doctor> doctors;

	private List<Hospital> hospitals;

	private List<DoctorType> doctorTypes;
	
	private String view;
	private String date;
	
	private Date currentDate;

	private long reservationCount;
	
	private TimeSlot timeSlot;
	
	@Inject
	private TimeSlotFacade timeSlotFacade;

	@Inject
	private DoctorFacade doctorFacade;
	
	@Inject 
	private PatientFacade patientFacade;
	
	@Inject 
	private UserFacade userFacade;
	
	@Inject
	private UserController user;
	
	private Patient patient;
	
	private City city;
	
	private Hospital hospital;
	
	private DoctorType doctorType;
	
	private Doctor doctor;
	
	public TimeSlotController() {
		super(TimeSlot.class);
	}
	
	@PostConstruct
	@Override
	public void init() {
		patient = patientFacade.find(user.getPatient().getId());

		//Doctor doctor = new Doctor();//doctorFacade.find(262);
		
		timeSlot = new TimeSlot();
		timeSlot.setDoctor(doctor);
		timeSlot.setPatient(patient);
		eventModel = new DefaultScheduleModel();
		view = MONTH_VIEW;

		currentDate = new Date();
		date = currentDate.toString();					
	}

	public void getTimeSlotsForPlace(){
		List<WorkingTime> workDays = timeSlotFacade.getConsulateWorkingTime(timeSlot.getDoctor());
		
		Calendar monthEnd = Calendar.getInstance();
		monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));		
		eventModel = new DefaultScheduleModel();
		
		for (Calendar currDate = Calendar.getInstance(); currDate.before(monthEnd); currDate.add(Calendar.DAY_OF_MONTH, 1)) {
			currDate.setTimeZone(TimeZone.getTimeZone("UTC+2"));
			Calendar startDate = (Calendar) currDate.clone();
			Calendar endDate = (Calendar) currDate.clone();
			
			addScheduleEvents(workDays, startDate, endDate);
		}
	}
	
	public void getTimeSlotsForService() {
		timeSlot.setDoctor(getDoctor());
		List<WorkingTime> workDays = timeSlotFacade.getConsulateWorkingTime(timeSlot.getDoctor());
		List<TimeCapacity> consulerServices = timeSlotFacade.getTimeCapacityService(timeSlot.getDoctor());
		
		Calendar monthEnd = Calendar.getInstance();
		monthEnd.set(Calendar.DAY_OF_YEAR, monthEnd.getActualMaximum(Calendar.DAY_OF_YEAR));		
		eventModel = new DefaultScheduleModel();		

		
		for (Calendar currDate = Calendar.getInstance(); currDate.before(monthEnd); currDate.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1)) {
			currDate.setTimeZone(TimeZone.getTimeZone("UTC+2"));
			Calendar startDate = (Calendar) currDate.clone();
			Calendar endDate = (Calendar) currDate.clone();
			
			for(TimeCapacity tc : consulerServices){	
				addScheduleEventsByService(workDays, startDate, endDate, tc.getDayOfWeek().getValue(), tc.getDoctor());
			}
		}	
	}

	public List<Hospital> getHospitalFotCity() {
		hospitals = userFacade.getHospitalsForCity(getCity().getId());
		
		return hospitals;
	}
	
	public List<DoctorType> getDocTypesForHospitals() {
		doctorTypes = userFacade.getDoctorTypesForHospital(getHospital().getId());
		
		return doctorTypes;
	}
	
	public List<Doctor> getDoctorsbyDocType() {
		doctors = userFacade.getDocotrotByDoctorType(getDoctorType().getId());
		
		return doctors;
	}
	
	private void addScheduleEvents(List<WorkingTime> workDays, Calendar startDate, Calendar endDate) {
		long timeDifference;
		long timeSlotsCount;
		int day;
		int hour;
		int minute;
		
		for (WorkingTime entry : workDays) {
			day = entry.getDayOfWeek().getValue();
			
			if (day == startDate.get(Calendar.DAY_OF_WEEK)-1) {
				for (BreakTime breakTime : entry.getBreakTime()) {

					timeDifference = breakTime.getEndDate().getTime() - breakTime.getStartTime().getTime();
					timeSlotsCount = timeSlotsCount(timeDifference);

					hour = getHours(breakTime.getStartTime());
					minute = getMinutes(breakTime.getStartTime());
					
					addEventsInTimeSlots(timeSlotsCount, startDate, endDate, hour, minute, day);
				}
			}
		}
	}
	
	private void addScheduleEventsByService(List<WorkingTime> workDays, Calendar startDate, Calendar endDate, int dayOfWeek, Doctor consulateCode) {
		long timeDifference;
		long timeSlotsCount;
		int day;
		int hour;
		int minute;
		
		for (WorkingTime entry : workDays) {
			day = entry.getDayOfWeek().getValue();
			//entry.getConsulate().getCode();
			if (dayOfWeek == day && entry.getDoctor().equals(consulateCode)) {
				for (BreakTime breakTime : entry.getBreakTime()) {

					timeDifference = breakTime.getEndDate().getTime() - breakTime.getStartTime().getTime();
					timeSlotsCount = timeSlotsCount(timeDifference);

					hour = getHours(breakTime.getStartTime());
					minute = getMinutes(breakTime.getStartTime());
					
					addEventsInTimeSlots(timeSlotsCount, startDate, endDate, hour, minute, dayOfWeek);
					
				}
			}
		}
	}
	
	public void addEventsInTimeSlots(long timeSlotsCount, Calendar startDate, Calendar endDate, int hour, int minute, int day){			
		long availablePlaces = 0;
		int counter = 0;

		while (counter != timeSlotsCount) {
			startDate.set(Calendar.HOUR_OF_DAY, hour);
			startDate.set(Calendar.MINUTE, minute);
			startDate.set(Calendar.DAY_OF_WEEK, day+1);		
			
			endDate.setTime(startDate.getTime());
			endDate.add(Calendar.MINUTE, 30);
			
			hour = endDate.get(Calendar.HOUR_OF_DAY);
			minute = endDate.get(Calendar.MINUTE);

			if(timeSlot.getDoctor().getDoctorType() != null){
				availablePlaces = timeSlotFacade.getTimeCapacityCountByDay(timeSlot.getDoctor().getDoctorType(), DayOfWeek.of(day));
				reservationCount = getReservationCountByTimeSlot(startDate.getTime());	
			}
			if(reservationCount < availablePlaces){
				eventModel.addEvent(new DefaultScheduleEvent(Long.toString(availablePlaces), startDate.getTime(), endDate.getTime(), DEFAULT_EVENT));

			}else{
				eventModel.addEvent(new DefaultScheduleEvent(Long.toString(availablePlaces), startDate.getTime(), endDate.getTime(), RESERVED_EVENT));
			}
			
			counter++;
		}
	}

	public long timeSlotsCount(long timeDifferenece){
		long minutesInterval = TimeUnit.MINUTES.convert(timeDifferenece, TimeUnit.MILLISECONDS);
		long timeSlotsCount = minutesInterval / 30;
		
		return timeSlotsCount;
	}
	
	public int getHours(Date date) {
		Calendar hour = Calendar.getInstance();
		hour.setTime(date);

		return hour.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinutes(Date date) {
		Calendar minutes = Calendar.getInstance();
		minutes.setTime(date);

		return minutes.get(Calendar.MINUTE);
	}

	public void addEvent(ActionEvent actionEvent) {
		if (event.getId() == null)
			eventModel.addEvent(event);
		else
			eventModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		DefaultScheduleEvent defaultEvent = new DefaultScheduleEvent();
		event = (DefaultScheduleEvent) selectEvent.getObject();

		List<ScheduleEvent> events = eventModel.getEvents();
		
		reservationCount = getReservationCountByTimeSlot(event.getStartDate());

		if (Integer.parseInt(event.getTitle()) == reservationCount) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorry", "There no more places for this date");
			addMessage(message);
		}
		
		for (ScheduleEvent ev : events) {
			if (event.getId().equals(ev.getId()) && Integer.parseInt(event.getTitle()) > reservationCount) {
				event.setStyleClass(FREE_EVENT);	
				//set select date to TimeSlot
				timeSlot.setDate(convertToLocalDateTime(event.getStartDate()));
			}
			else if(ev.getStyleClass().equals(RESERVED_EVENT)){
				defaultEvent = (DefaultScheduleEvent)ev;
				defaultEvent.setStyleClass(RESERVED_EVENT);	
			}
			else{
				defaultEvent = (DefaultScheduleEvent)ev;
				defaultEvent.setStyleClass(DEFAULT_EVENT);
			}
			
		}
	}

//	public void getEventsDay(ScheduleModel model) {
//		List<ScheduleEvent> events = model.getEvents();
//		for (ScheduleEvent scheduleEvent : events) {
//			if(Integer.parseInt(scheduleEvent.getDescription()) != 0){
//				
//			}
//		}
//	}
	
	public void onDateSelect(SelectEvent selectEvent) {
		Date todayDate = new Date();
		currentDate = (Date) selectEvent.getObject();

		long millisecTime = currentDate.getTime() - todayDate.getTime();
		long daysDifference = TimeUnit.DAYS.convert(millisecTime, TimeUnit.MILLISECONDS);

		 if(daysDifference < APPLY_DAYS_COUNT){
			 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorry", "you can select two days forward from current day");
			 addMessage(message);
		 }
		 else{
			 view = AGENDA_DAY_VIEW;
		 }
	}

	private long getReservationCountByTimeSlot(Date startDate) {
		LocalDateTime localDT = convertToLocalDateTime(startDate);
		
		return timeSlotFacade.getCountOfReservation(localDT, timeSlot.getDoctor().getDoctorType().getType());
	}

	private LocalDateTime convertToLocalDateTime(Date startDate) {
		DateTimeFormatter dtFormater = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

		LocalDateTime convertDate = startDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
		String convertDateToStr = dtFormater.format(convertDate);
		LocalDateTime formatedDate = LocalDateTime.parse(convertDateToStr, dtFormater);
		
		return formatedDate;
	}
	
	public String save(){
//		patient.setFirstName(getSelected().getPatient().getFirstName());
//		patient.setLastName(getSelected().getPatient().getFirstName());
//		patient.setPhoneNumber(getSelected().getPatient().getPhoneNumber());
//		patient.setEmail(getSelected().getPatient().getPhoneNumber());
//		
//		timeSlot.setPatient(patient);
		
		Set<Patient> patients = new HashSet<>();
		patients.add(timeSlot.getPatient());
		
		timeSlot.getDoctor().setPatients(patients);
		
		
		doctorFacade.edit(timeSlot.getDoctor());
		timeSlotFacade.create(timeSlot);

		return "shedule.xhtml";	
	}
	 
	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(DefaultScheduleEvent event) {
		this.event = event;
	}
	public String getView() {
		return view;
	}

	public void setView(String agendaDay) {
		this.view = agendaDay;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(List<Doctor> doctors) {
		this.doctors = doctors;
	}

	public List<Hospital> getHospitals() {
		return hospitals;
	}

	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public DoctorType getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(DoctorType doctorType) {
		this.doctorType = doctorType;
	}

	public List<DoctorType> getDoctorTypes() {
		return doctorTypes;
	}

	public void setDoctorTypes(List<DoctorType> doctorTypes) {
		this.doctorTypes = doctorTypes;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
}
