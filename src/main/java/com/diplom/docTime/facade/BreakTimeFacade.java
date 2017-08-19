package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.BreakTime;


public class BreakTimeFacade extends AbstractFacade<BreakTime>{

	private static final long serialVersionUID = 1L;

	public BreakTimeFacade() {
		super(BreakTime.class);
	}

	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
