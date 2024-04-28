package es.deusto.sqp.dao;

import es.deusto.spq.serialization.Customer;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class CustomerDAO extends DataAccessObject implements IDataAccessObject<Customer> {
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
	public void store(Customer object) {
		Customer storedObject = instance.find(object.geteMail());

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
	public void delete(Customer object) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			
			Customer storedObject = (Customer) em.find(Customer.class, object.geteMail());
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
	public List<Customer> findAll() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		List<Customer> users = new ArrayList<>();

		try {
			tx.begin();
			//TODO: modify query to work properly
			Query q = em.createQuery("SELECT u FROM Customer u");
			users = (List<Customer>) q.getResultList();
						
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
	public Customer find(String param) {		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		Customer result = null; 

		try {
			tx.begin();

			result = (Customer) em.find(Customer.class, param);
			
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

