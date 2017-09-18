package com.diplom.docTime.converter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.model.Doctor;

@Named
public class DoctorConverter  extends AbstractConverter<Integer, Doctor>{

	@Inject
	private DoctorFacade doctorFacade;
	
	@Override
	@PostConstruct
	public void init() {
		this.setFacade(doctorFacade);
		super.initTypes(Integer.class, Doctor.class, "getId");

	}

}
