package com.diplom.docTime.controller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.DocTypeFacade;
import com.diplom.docTime.facade.DoctorFacade;
import com.diplom.docTime.model.DoctorType;

@Named
public class DoctorTypeController extends AbstractController<DoctorType>{

	private static final long serialVersionUID = 7850472756177310680L;

	@Inject
	private DocTypeFacade docTypeFacade;
	
	@Override
	@PostConstruct
	public void init() {
		this.setFacade(docTypeFacade);
	}

	public DoctorTypeController() {
		super(DoctorType.class);
	}
}
