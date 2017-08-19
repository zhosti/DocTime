package com.diplom.docTime.controller;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.diplom.docTime.facade.BreakTimeFacade;
import com.diplom.docTime.facade.WorkTimeFacade;
import com.diplom.docTime.model.BreakTime;
import com.diplom.docTime.model.WorkingTime;


public class BreakTimeController extends AbstractController<BreakTime>{

	private static final long serialVersionUID = 1L;

	@Inject
	private BreakTimeFacade breakTimeFacade;
	
	@Inject
	private WorkTimeFacade workTimeFacade;
	
	@PostConstruct
	@Override
	public void init() {
		super.setFacade(breakTimeFacade);
		//getDaysWorkTime();
	}

	public BreakTimeController(){
		super(BreakTime.class);
	}
	
	public Map<DayOfWeek, List<BreakTime>> getDaysWorkTime(){

		Map<DayOfWeek, List<BreakTime>> scheduleEvents = new HashMap<>();
		
		List<WorkingTime> workTime = workTimeFacade.findAll();
		
		for(WorkingTime wt : workTime){

			scheduleEvents.put(wt.getDayOfWeek(), wt.getBreakTime());
//			for(BreakTime bt : wt.getBreakTime()){
//				System.out.println(wt.getDay() + " " + bt.getStartTime() + " " + bt.getEndTime());
//			}
			
			//System.out.println(wt.getDay() + " " + wt.getBreakTime());
		}
		return scheduleEvents;
	}
}