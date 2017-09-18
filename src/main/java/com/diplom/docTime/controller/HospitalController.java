package com.diplom.docTime.controller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.diplom.docTime.facade.HospitalFacade;
import com.diplom.docTime.model.Hospital;

@Named
public class HospitalController extends AbstractController<Hospital>{

	private static final long serialVersionUID = 2826477621406771339L;

	@Inject
	private HospitalFacade hospitalFacade;
	
	@Override
	@PostConstruct
	public void init() {
		this.setFacade(hospitalFacade);
		
	}

	public HospitalController() {
		super(Hospital.class);
	}
}
