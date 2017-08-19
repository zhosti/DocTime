package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.TimeCapacity;

public class TimeCapacityFacade extends AbstractFacade<TimeCapacity> {

	private static final long serialVersionUID = 1L;

	public TimeCapacityFacade() {
		super(TimeCapacity.class);
	}

	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
