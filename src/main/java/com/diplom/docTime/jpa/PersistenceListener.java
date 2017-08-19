package com.diplom.docTime.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PersistenceListener implements ServletContextListener {

	private static EntityManagerFactory entityManagerFactory;


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		entityManagerFactory = Persistence.createEntityManagerFactory("primary");
		createEntityManager();
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}


	@Produces
	public static EntityManager createEntityManager() {
		if (entityManagerFactory == null) {
			throw new IllegalStateException("Context is not initialized yet.");
		}

		return entityManagerFactory.createEntityManager();
	}

}
