package com.diplom.docTime.converter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.DocTypeFacade;
import com.diplom.docTime.facade.HospitalFacade;
import com.diplom.docTime.model.DoctorType;
import com.diplom.docTime.model.Hospital;

@Named
public class HospitalConverter extends AbstractConverter<Integer, Hospital>{

	@Inject
	private HospitalFacade hospitalFacade;
	
	@Override
	@PostConstruct
	public void init() {
		this.setFacade(hospitalFacade);
		super.initTypes(Integer.class, Hospital.class, "getId");

	}

}