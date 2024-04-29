package es.deusto.spq.dao;

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.serialization.Customer;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class CustomerDAO extends DataAccessObject implements IDataAccessObject<CustomerData> {
	//This class implements Singleton and DAO patterns
	private static CustomerDAO instance;	
		
	private CustomerDAO() { }
		
	public static CustomerDAO getInstance() {
		if (instance == null) {
			instance = new CustomerDAO();
		}		
			
		return instance;
	}	
		
	@Override
	public void store(CustomerData object) {
		CustomerData storedObject = instance.find(object.geteMail());

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
				
			if (storedObject != null) {
				em.merge(object);
			} else {
				em.persist(object);
			}
				
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error storing Customer: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}

	@Override
	public void delete(CustomerData object) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			
			CustomerData storedObject = (CustomerData) em.find(CustomerData.class, object.geteMail());
			em.remove(storedObject);
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error removing a Customer: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerData> findAll() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		List<CustomerData> users = new ArrayList<>();

		try {
			tx.begin();
			//TODO: modify query to work properly
			Query q = em.createQuery("SELECT u FROM Customer u");
			users = (List<CustomerData>) q.getResultList();
						
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error querying all customers: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}

		return users;
	}

	@Override
	public CustomerData find(String param) {		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		CustomerData result = null; 

		try {
			tx.begin();

			result = (CustomerData) em.find(CustomerData.class, param);
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error querying a Customer by email: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}

		return result;
	}
}

