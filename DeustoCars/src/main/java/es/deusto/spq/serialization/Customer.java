package es.deusto.spq.serialization;

import java.time.LocalDate;
import java.util.Date;

/*
 * Class of the customer
 * */

public class Customer {
    private String eMail;
    private String name;
    private String surname;
    private Date dateOfBirth;

    /*
     * Constructor of the customer class
     * */


    public Customer(String eMail, String name, String surname, Date dateOfBirth) {
        this.eMail = eMail;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }
    public Customer(String eMail, String name, String surname) {
        this.eMail = eMail;
        this.name = name;
        this.surname = surname;
    }
    
    /*
     * Getters and setters for the customer class
     * */
    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

	public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

	@Override
	public String toString() {
		return "Customer [eMail=" + eMail + ", name=" + name + ", surname=" + surname + "]";//", dateOfBirth=" + dateOfBirth + "]";
	}

    
    /*
     * Returns a string representation of the Customer object.
     * */
    
    
}