package com.diplom.docTime.controller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.CityFacade;
import com.diplom.docTime.model.City;

@Named
public class CityController extends AbstractController<City>{

	private static final long serialVersionUID = -3658271739828256048L;
	
	@Inject
	private CityFacade cityFacade;
	
	@Override
	@PostConstruct
	public void init() {

		this.setFacade(cityFacade);
	}

	public CityController() {
		super(City.class);
	}
}
