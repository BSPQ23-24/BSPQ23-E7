package es.deusto.spq.serialization;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a customer.
 */
@Entity
@Table(name = "customers") // Specifies table name
public class Customer {
    @Id
    @Column(name = "email", nullable = false)
    private String eMail;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    // Default constructor required for JPA
    public Customer() {}

    /**
     * Constructs a Customer object with the specified email, name, surname, and date of birth.
     *
     * @param eMail       The email of the customer.
     * @param name        The name of the customer.
     * @param surname     The surname of the customer.
     * @param dateOfBirth The date of birth of the customer.
     */
    public Customer(String eMail, String name, String surname, Date dateOfBirth) {
        this.eMail = eMail;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructs a Customer object with the specified email, name, and surname.
     *
     * @param eMail   The email of the customer.
     * @param name    The name of the customer.
     * @param surname The surname of the customer.
     */
    public Customer(String eMail, String name, String surname) {
        this.eMail = eMail;
        this.name = name;
        this.surname = surname;
    }
    
    /**
     * Retrieves the email of the customer.
     *
     * @return The email of the customer.
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * Sets the email of the customer.
     *
     * @param eMail The email to set.
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * Retrieves the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the surname of the customer.
     *
     * @return The surname of the customer.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the customer.
     *
     * @param surname The surname to set.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Retrieves the date of birth of the customer.
     *
     * @return The date of birth of the customer.
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the customer.
     *
     * @param dateOfBirth The date of birth to set.
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns a string representation of the Customer object.
     *
     * @return A string representation of the Customer object.
     */
    @Override
    public String toString() {
        return "Customer{" +
               "eMail='" + eMail + '\'' +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               //", dateOfBirth=" + dateOfBirth +
               '}';
    }
}
