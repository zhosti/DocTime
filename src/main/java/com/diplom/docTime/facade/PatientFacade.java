package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.Patient;

public class PatientFacade extends AbstractFacade<Patient>{


	private static final long serialVersionUID = -4991372897266458919L;

	public PatientFacade() {
		super(Patient.class);
	}
	
	@Inject
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	
}
