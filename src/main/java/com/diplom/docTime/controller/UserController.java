package com.diplom.docTime.controller;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.facade.PatientFacade;
import com.diplom.docTime.facade.UserFacade;
import com.diplom.docTime.model.City;
import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.DoctorType;
import com.diplom.docTime.model.Hospital;
import com.diplom.docTime.model.Patient;
import com.diplom.docTime.model.Prescription;
import com.diplom.docTime.model.TimeSlot;
import com.diplom.docTime.model.User;

@SessionScoped
@Named(value = "userController")
public class UserController extends AbstractController<User> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4135694402956549707L;

	@Inject
	private UserFacade userFacade;

	@Inject
	private DoctorFacade doctorFacade;

	@Inject
	private PatientFacade patientFacade;

	private Patient patient;

	private Doctor doctor;

	private Hospital hospital;

	private City city;

	private DoctorType doctorType;

	private boolean isDoctorUser;

	private String username;

	private String password;

	private String prescription;

	private boolean loggedIn;

	@Override
	@PostConstruct
	public void init() {
		// super.setFacade(userFacade);
		doctor = new Doctor();

		hospital = new Hospital();
		city = new City();
		doctorType = new DoctorType();

		doctor.setHospital(getHospital());
		doctor.getHospital().setCity(getCity());
		doctor.setDoctorType(getDoctorType());

		patient = new Patient();
		isDoctorUser = true;
	}

	public UserController() {
		super(User.class);
	}

	public void registerUser() throws NoSuchAlgorithmException {
		if (isDoctorUser) {
			StringBuffer stringBuffer = passwordEncryption(doctor.getPassword().getBytes());
			doctor.setPassword(stringBuffer.toString());
			doctorFacade.create(doctor);
		} else {
			StringBuffer stringBuffer = passwordEncryption(patient.getPassword().getBytes());
			patient.setPassword(stringBuffer.toString());

			patientFacade.create(patient);
		}
	}

	private StringBuffer passwordEncryption(byte[] pass) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(pass);
		byte[] messageDigestMD5 = md.digest();

		StringBuffer stringBuffer = new StringBuffer();
		for (byte bytes : messageDigestMD5) {

			stringBuffer.append(String.format("%02x", bytes));

		}
		return stringBuffer;
	}

	public String login() throws NoSuchAlgorithmException {
		StringBuffer pass = passwordEncryption(password.getBytes());

		Doctor doctor = userFacade.findDocotrUserByUsernameAndPassword(username, pass.toString());
		Patient patient = userFacade.findPatientUserByUsernameAndPassword(username, pass.toString());

		if (doctor != null) {
			if (doctor.getUsername().equals(username) && doctor.getPassword().equals(pass.toString()) && isDoctorUser) {
				System.out.println("Success");
				isDoctorUser = true;
				loggedIn = true;
				setDoctor(doctor);
				return "success";
			} else {
				// FacesContext facesContext =
				// FacesContext.getCurrentInstance();
				// facesContext.addMessage(null,
				// new FacesMessage(FacesMessage.SEVERITY_WARN, "Username or
				// password is incorrect", null));
				return "error";
			}

		} else {
			if (patient.getUsername().equals(username) && patient.getPassword().equals(pass.toString())
					&& !isDoctorUser) {
				System.out.println("Success");
				isDoctorUser = false;
				loggedIn = true;
				setPatient(patient);
				return "success";
			} else {
				// FacesContext facesContext =
				// FacesContext.getCurrentInstance();
				// facesContext.addMessage(null,
				// new FacesMessage(FacesMessage.SEVERITY_WARN, "Username or
				// password is incorrect", null));
				return "error";
			}
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login.xhtml?faces-redirect=true";
	}

	public void save() {
		Prescription prescript = new Prescription();
		prescript.setText(getPrescription());

		List<Prescription> prescriptions = new ArrayList<>();
		prescriptions.add(prescript);

		getPatient().setPrescription(prescriptions);

		userFacade.edit(getPatient());

	}

	public List<Patient> getDoctorReservation() {
		List<Patient> patients = userFacade.getAllDoctorPatients(getDoctor().getId());

		return patients;
	}

	public List<TimeSlot> getAllPatientPrescriptions() {
		List<TimeSlot> tSlots = userFacade.getPatientPrescription(getPatient().getId());
		
		return tSlots;
	}

	public boolean getDoctorUser() {
		return isDoctorUser;
	}

	public void setDoctorUser(boolean isDoctorUser) {
		this.isDoctorUser = isDoctorUser;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public DoctorType getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(DoctorType doctorType) {
		this.doctorType = doctorType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

}
