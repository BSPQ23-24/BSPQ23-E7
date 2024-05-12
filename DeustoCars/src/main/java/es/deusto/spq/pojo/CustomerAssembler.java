package es.deusto.spq.pojo;

import java.util.ArrayList;
import java.util.List;

import es.deusto.spq.server.jdo.CustomerJDO;

public class CustomerAssembler {
	
	// Singleton
	private static CustomerAssembler instance;
	
	private CustomerAssembler() { }
	
	public static CustomerAssembler getInstance() {
		if (instance == null) {
			instance = new CustomerAssembler();
		}
		
		return instance;
	}
	
    /**
     * Changes the CustomerJDO to CustomerData.
     * 
     * @param customer The customer JDO to change.
     * @return CustomerData with all the necessary information.
     */
	public CustomerData CustomerJDOToData(CustomerJDO customer) {
		CustomerData data = new CustomerData();		
		
		data.setName(customer.getName());
		data.seteMail(customer.geteMail());
		data.setSurname(customer.getSurname());
		data.setDateOfBirth(customer.getDateOfBirth());
		
		return data;
	}
	
	/**
     * Changes a CustomerJDO list to a CustomerData list.
     * 
     * @param customers The customer JDO list to change.
     * @return CustomerData list with all the necessary information.
     */
	public List<CustomerData> CustomerJDOListToData(List<CustomerJDO> customers){
		List<CustomerData> data = new ArrayList<CustomerData>();
    	for (CustomerJDO jdo : customers) {
			data.add(CustomerAssembler.getInstance().CustomerJDOToData(jdo));
		}
    	
    	return data;
	}
	
    /**
     * Changes the CustomerData to CustomerJDO.
     * 
     * @param customer The customer data to change.
     * @return CustomerJDO with all the necessary information.
     */
	public CustomerJDO CustomerDataToJDO(CustomerData customer) {
		CustomerJDO jdo = new CustomerJDO();		
		
		jdo.setName(customer.getName());
		jdo.seteMail(customer.geteMail());
		jdo.setSurname(customer.getSurname());
		jdo.setDateOfBirth(customer.getDateOfBirth());
		
		return jdo;
	}
}
