package es.deusto.spq.dao;

import es.deusto.spq.serialization.Customer;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CustomerDAO {
    private static CustomerDAO instance;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("DeustoCars");
    
    private CustomerDAO() { }
    
    public static CustomerDAO getInstance() {
        if (instance == null) {
            instance = new CustomerDAO();
        }       
        return instance;
    }   
    
    public void store(Customer customer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            if (em.find(Customer.class, customer.geteMail()) != null) {
                em.merge(customer);
            } else {
                em.persist(customer);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error storing customer", ex);
        } finally {
            em.close();
        }
    }

    public void delete(String email) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Customer customer = em.find(Customer.class, email);
            if (customer != null) {
                em.remove(customer);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting customer", ex);
        } finally {
            em.close();
        }
    }

    public List<Customer> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Customer find(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Customer.class, email);
        } finally {
            em.close();
        }
    }
}
