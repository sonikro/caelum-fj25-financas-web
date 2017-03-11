package br.com.caelum.financas.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	
	@Produces @RequestScoped
	public EntityManager createEntityManager()
	{
		return emf.createEntityManager();
	}
	
	public void dispose(@Disposes EntityManager manager)
	{
		manager.close();
	}
}
