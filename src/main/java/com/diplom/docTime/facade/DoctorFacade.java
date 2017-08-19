package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.DoctorType;

public class DoctorFacade extends AbstractFacade<Doctor>{

	private static final long serialVersionUID = 1L;

	public DoctorFacade() {
		super(Doctor.class);
	}

	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}