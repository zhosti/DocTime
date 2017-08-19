package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.DoctorType;

public class DocTypeFacade extends AbstractFacade<DoctorType>{

	private static final long serialVersionUID = 1L;

	public DocTypeFacade() {
		super(DoctorType.class);
	}

	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
