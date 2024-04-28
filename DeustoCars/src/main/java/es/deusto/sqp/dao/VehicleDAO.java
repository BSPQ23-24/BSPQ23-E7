package es.deusto.sqp.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import es.deusto.spq.serialization.Vehicle;

public class VehicleDAO extends DataAccessObject implements IDataAccessObject<Vehicle> {
	//This class implements Singleton and DAO patterns
	private static VehicleDAO instance;	
		
	private VehicleDAO() { }
		
	public static VehicleDAO getInstance() {
		if (instance == null) {
			instance = new VehicleDAO();
		}		
			
		return instance;
	}	
		
	@Override
	public void store(Vehicle object) {
		Vehicle storedObject = instance.find(object.getNumberPlate());

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
			System.out.println("  $ Error storing Vehicle: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}

	@Override
	public void delete(Vehicle object) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			
			Vehicle storedObject = (Vehicle) em.find(Vehicle.class, object.getNumberPlate());
			em.remove(storedObject);
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error removing a Vehicle: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vehicle> findAll() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		List<Vehicle> users = new ArrayList<>();

		try {
			tx.begin();
			//TODO: modify query to work properly
			Query q = em.createQuery("SELECT u FROM Vehicle u");
			users = (List<Vehicle>) q.getResultList();
						
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error querying all Vehicles: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}

		return users;
	}

	@Override
	public Vehicle find(String param) {		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		Vehicle result = null; 

		try {
			tx.begin();

			result = (Vehicle) em.find(Vehicle.class, param);
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error querying a Vehicle by plate number: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}

		return result;
	}
}
