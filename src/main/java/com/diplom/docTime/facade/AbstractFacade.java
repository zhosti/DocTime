package com.diplom.docTime.facade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * The Class AbstractFacade.
 *
 * @author Strahil Vitkov
 * @param <T>
 *            the generic type
 */
public abstract class AbstractFacade<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	/** The entity class. */
	private Class<T> entityClass;

	/**
	 * Instantiates a new abstract facade.
	 *
	 * @param entityClass
	 *            the entity class
	 */
	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	protected abstract EntityManager getEntityManager();

	/**
	 * Creates the.
	 *
	 * @param entity
	 *            the entity
	 */
	public void create(T entity) {
		getEntityManager().getTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().getTransaction().commit();
	}

	/**
	 * Edits the.
	 *
	 * @param entity
	 *            the entity
	 * @return the t
	 */
	public T edit(T entity) {
		getEntityManager().getTransaction().begin();
		T mergedEntity = getEntityManager().merge(entity);
		getEntityManager().getTransaction().commit();
		return mergedEntity;
	}

	/**
	 * Removes the.
	 *
	 * @param entity
	 *            the entity
	 */
	public void remove(T entity) {
		getEntityManager().getTransaction().begin();
		getEntityManager().remove(getEntityManager().merge(entity));
		getEntityManager().getTransaction().commit();
		
	}

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the t
	 */
	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<T> findAll() {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder()
				.createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	/**
	 * Find range.
	 *
	 * @param range
	 *            the range
	 * @return the list
	 */
	@Deprecated
	public List<T> findRange(int[] range) {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder()
				.createQuery(entityClass);
		cq.select(cq.from(entityClass));
		TypedQuery<T> q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	/**
	 * Count.
	 *
	 * @return the int
	 */
	public int count() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> rt = cq.from(entityClass);
		cq.select(cb.count(rt));
		Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	/**
	 * Gets the entity class.
	 *
	 * @return the entity class
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	public AbstractFacade() {

	}
}
