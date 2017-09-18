package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.Hospital;

public class HospitalFacade extends AbstractFacade<Hospital>{

	private static final long serialVersionUID = -2107125567611138894L;

	
	public HospitalFacade() {
		super(Hospital.class);
	}
	
	@Inject
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
