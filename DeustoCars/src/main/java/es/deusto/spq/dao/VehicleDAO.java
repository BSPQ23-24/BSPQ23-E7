package es.deusto.spq.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import es.deusto.spq.pojo.VehicleData;

public class VehicleDAO extends DataAccessObject implements IDataAccessObject<VehicleData> {
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
	public void store(VehicleData object) {
		VehicleData storedObject = instance.find(object.getNumberPlate());

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
			System.out.println("  $ Error storing VehicleData: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}

	@Override
	public void delete(VehicleData object) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			
			VehicleData storedObject = (VehicleData) em.find(VehicleData.class, object.getNumberPlate());
			em.remove(storedObject);
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error removing a VehicleData: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleData> findAll() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		List<VehicleData> users = new ArrayList<>();

		try {
			tx.begin();
			//TODO: modify query to work properly
			Query q = em.createQuery("SELECT u FROM VehicleData u");
			users = (List<VehicleData>) q.getResultList();
						
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
	public VehicleData find(String param) {		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		VehicleData result = null; 

		try {
			tx.begin();

			result = (VehicleData) em.find(VehicleData.class, param);
			
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
