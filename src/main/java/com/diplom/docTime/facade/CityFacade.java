package com.diplom.docTime.facade;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.diplom.docTime.model.City;

public class CityFacade extends AbstractFacade<City>{


	private static final long serialVersionUID = 8884245823582157162L;

	public CityFacade() {
		super(City.class);
	}
	
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
