package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.WorkingTime;

public class WorkTimeFacade extends AbstractFacade<WorkingTime> {

	private static final long serialVersionUID = 1L;

	public WorkTimeFacade() {
		super(WorkingTime.class);
	}

	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
