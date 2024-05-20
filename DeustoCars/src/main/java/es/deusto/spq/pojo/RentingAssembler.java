package es.deusto.spq.pojo;

import java.util.ArrayList;
import java.util.List;

import es.deusto.spq.server.jdo.RentingJDO;

public class RentingAssembler {
	// Singleton
		private static RentingAssembler instance;
		
		private RentingAssembler() { }
		
		public static RentingAssembler getInstance() {
			if (instance == null) {
				instance = new RentingAssembler();
			}
			
			return instance;
		}
		
	    /**
	     * Changes the RentingJDO to RentingData.
	     * 
	     * @param Renting The Renting JDO to change.
	     * @return RentingData with all the necessary information.
	     */
		public Renting RentingJDOToData(RentingJDO Renting) {
			Renting data = new Renting();		
			data.setCustomer(CustomerAssembler.getInstance().CustomerJDOToData(Renting.getCustomer()));
			data.setVehicle(VehicleAssembler.getInstance().VehicleJDOToData(Renting.getVehicle()));
			data.setStartDate(Renting.getStartDate());
			data.setEndDate(Renting.getEndDate());
			
			return data;
		}
		
		/**
	     * Changes a RentingJDO list to a RentingData list.
	     * 
	     * @param Rentings The Renting JDO list to change.
	     * @return RentingData list with all the necessary information.
	     */
		public List<Renting> RentingJDOListToData(List<RentingJDO> Rentings){
			List<Renting> data = new ArrayList<Renting>();
	    	for (RentingJDO jdo : Rentings) {
				data.add(RentingAssembler.getInstance().RentingJDOToData(jdo));
			}
	    	
	    	return data;
		}
		
}
