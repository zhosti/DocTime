package com.diplom.docTime.converter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.CityFacade;
import com.diplom.docTime.facade.DocTypeFacade;
import com.diplom.docTime.model.City;
import com.diplom.docTime.model.DoctorType;

@Named
public class CityConverter extends AbstractConverter<Integer, City>{

	@Inject
	private CityFacade cityFacade;
	
	@Override
	@PostConstruct
	public void init() {
		this.setFacade(cityFacade);
		super.initTypes(Integer.class, City.class, "getId");

	}

}