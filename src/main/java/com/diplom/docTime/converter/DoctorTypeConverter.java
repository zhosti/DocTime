package com.diplom.docTime.converter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.DocTypeFacade;
import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.model.DoctorType;


@Named(value="docTypeConv")
public class DoctorTypeConverter extends AbstractConverter<Integer, DoctorType>{

	@Inject
	private DocTypeFacade doctorTypeFacade;
	
	@Override
	@PostConstruct
	public void init() {
		this.setFacade(doctorTypeFacade);
		super.initTypes(Integer.class, DoctorType.class, "getId");

	}

}
