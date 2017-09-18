package com.diplom.docTime.facade;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.DoctorType;
import com.diplom.docTime.model.Hospital;
import com.diplom.docTime.model.Patient;
import com.diplom.docTime.model.TimeCapacity;
import com.diplom.docTime.model.TimeSlot;
import com.diplom.docTime.model.User;

public class UserFacade extends AbstractFacade<User>{


	private static final long serialVersionUID = -3484761918404570777L;

	public UserFacade() {
		super(User.class);
	}
	
	@Inject
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	
	public Doctor findDocotrUserByUsernameAndPassword(String username, String password){
		try {
			String strQuery = "select u from Doctor u where u.username = :username and u.password = :password";
			TypedQuery<Doctor> query= getEntityManager().createQuery(strQuery, Doctor.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			
			Doctor user = query.getSingleResult();
			
			return user;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Patient findPatientUserByUsernameAndPassword(String username, String password){
		try {
			String strQuery = "select u from Patient u where u.username = :username and u.password = :password";
			TypedQuery<Patient> query= getEntityManager().createQuery(strQuery, Patient.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			
			Patient user = query.getSingleResult();
			
			return user;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Patient> getAllDoctorPatients(int id) {
		String strQuery = "select t.patient from TimeSlot t join Doctor d on d.id = t.doctor.id where d.id = :doctorId";

		TypedQuery<Patient> query = getEntityManager().createQuery(strQuery, Patient.class);
		query.setParameter("doctorId", id);
		
		List<Patient> patients = query.getResultList();
		
		return patients;
	}
	
	public List<TimeSlot> getPatientPrescription(int id) {
		String strQuery = "select t from TimeSlot t join Patient p on p.id = t.patient.id where p.id = :patientId";

		TypedQuery<TimeSlot> query = getEntityManager().createQuery(strQuery, TimeSlot.class);
		query.setParameter("patientId", id);
		
		List<TimeSlot> patientsTS = query.getResultList();
		
		return patientsTS;
	}
	
	public List<DoctorType> getDoctorTypesForHospital(int hospitalId) {
		String strQuery = "select dt from Doctor d join DoctorType dt on dt.id = d.doctorType.id where d.hospital.Id = :hospitalId";
		
		TypedQuery<DoctorType> query = getEntityManager().createQuery(strQuery, DoctorType.class);
		query.setParameter("hospitalId", hospitalId);
		
		List<DoctorType> doctorTypes = query.getResultList();
		
		return doctorTypes;
	}
	
	public List<Doctor> getDocotrotByDoctorType(int docTypeId) {
		String strQuery = "select d from Doctor d where d.doctorType.id = :docTypeId";
		
		TypedQuery<Doctor> query = getEntityManager().createQuery(strQuery, Doctor.class);
		query.setParameter("docTypeId", docTypeId);
		
		List<Doctor> doctors = query.getResultList();
		
		return doctors;
	}
	
	public List<Hospital> getHospitalsForCity(int cityId) {
		String strQuery = "select h from Hospital h where h.City.id = :cityId";
		
		TypedQuery<Hospital> query = getEntityManager().createQuery(strQuery, Hospital.class);
		query.setParameter("cityId", cityId);
		
		List<Hospital> hospitals = query.getResultList();
		
		return hospitals;
	}
}
