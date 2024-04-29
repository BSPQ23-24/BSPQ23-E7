package es.deusto.spq.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DataAccessObject {	
	protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory("DeustoCars");
	
	public void deleteObject(Object object) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();			
			em.remove(object);						
			tx.commit();
		} catch (Exception ex) {
			System.out.println(String.format(" $ Error deleting an object (%s): %s", 
					object.toString(), ex.getMessage()));
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}
	
	public void saveObject(Object object) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(object);
			tx.commit();
		} catch (Exception ex) {
			System.out.println(String.format(" $ Error storing an object (%s): %s", 
					object.toString(), ex.getMessage()));

		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}
}
