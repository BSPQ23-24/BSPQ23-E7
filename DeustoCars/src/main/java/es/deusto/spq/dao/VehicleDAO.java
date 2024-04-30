package es.deusto.spq.dao;

import es.deusto.spq.serialization.Vehicle;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class VehicleDAO extends DataAccessObject {
    private static VehicleDAO instance;

    private VehicleDAO() {}

    public static VehicleDAO getInstance() {
        if (instance == null) {
            instance = new VehicleDAO();
        }
        return instance;
    }

    public void store(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (em.find(Vehicle.class, vehicle.getNumberPlate()) != null) {
                em.merge(vehicle);
            } else {
                em.persist(vehicle);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error storing vehicle: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    public void delete(String numberPlate) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Vehicle vehicle = em.find(Vehicle.class, numberPlate);
            if (vehicle != null) {
                em.remove(vehicle);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error removing vehicle: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    public List<Vehicle> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Vehicle find(String numberPlate) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Vehicle.class, numberPlate);
        } finally {
            em.close();
        }
    }
}
