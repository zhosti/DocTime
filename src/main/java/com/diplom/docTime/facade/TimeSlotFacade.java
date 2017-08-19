package com.diplom.docTime.facade;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.diplom.docTime.model.Doctor;
import com.diplom.docTime.model.DoctorType;
import com.diplom.docTime.model.TimeCapacity;
import com.diplom.docTime.model.TimeSlot;
import com.diplom.docTime.model.WorkingTime;



public class TimeSlotFacade extends AbstractFacade<TimeSlot> {

	private static final long serialVersionUID = 1L;

	public TimeSlotFacade() {
		super(TimeSlot.class);
	}

	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<WorkingTime> getConsulateWorkingTime(Doctor param) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<WorkingTime> query = criteriaBuilder.createQuery(WorkingTime.class);
		Root<WorkingTime> workingTime = query.from(WorkingTime.class);

		query.select(workingTime);
		query.where(criteriaBuilder.equal(workingTime.get("doctor"), param));

		Query resultQuery = getEntityManager().createQuery(query);

		return resultQuery.getResultList();
	}

	public List<TimeCapacity> getTimeCapacityService(Doctor param) {
		String strQuery = "select t from TimeCapacity t join t.doctor s where s.doctorType = :doctorType and s.count > 0";
		TypedQuery<TimeCapacity> query = getEntityManager().createQuery(strQuery, TimeCapacity.class);
		query.setParameter("doctorType", param.getDoctorType());
		List<TimeCapacity> timeCapacities = query.getResultList();

		return timeCapacities;
	}

	public Integer getTimeCapacityCountByDay(DoctorType param, DayOfWeek dayOfWeek) {
//		String strQuery = "select s.count from TimeCapacity t join t.services s where s.serviceNom.code = :serviceNom and t.dayOfWeek = :dayOfweek";
//		TypedQuery<Integer> query = getEntityManager().createQuery(strQuery, Integer.class);
//
//		query.setParameter("serviceNom", param.getCode());
//		query.setParameter("dayOfweek", dayOfWeek);
//
//		Integer availablePlaces = query.getSingleResult();

		return 0;
	}

	public long getCountOfReservation(LocalDateTime date, String serviceCode) {
		String strQuery = "select count(*) from TimeSlot ts where ts.date = :date and ts.service.code = :serviceCode";
		TypedQuery<Long> query = getEntityManager().createQuery(strQuery, Long.class);

		query.setParameter("date", date);
		query.setParameter("serviceCode", serviceCode);

		long count = query.getSingleResult();

		return count;
	}
}
