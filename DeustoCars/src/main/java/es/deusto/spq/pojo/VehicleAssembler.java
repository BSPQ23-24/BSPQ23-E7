package es.deusto.spq.pojo;

import java.util.ArrayList;
import java.util.List;

import es.deusto.spq.server.jdo.VehicleJDO;

public class VehicleAssembler {
	
	// Singleton
	private static VehicleAssembler instance;
	
	private VehicleAssembler() { }
	
	public static VehicleAssembler getInstance() {
		if (instance == null) {
			instance = new VehicleAssembler();
		}
		
		return instance;
	}
	
    /**
     * Changes the VehicleJDO to VehicleData.
     * 
     * @param vehicle The vehicle JDO to change.
     * @return VehicleData with all the necessary information.
     */
	public VehicleData VehicleJDOToData(VehicleJDO vehicle) {
		VehicleData data = new VehicleData();		
		
		data.setBrand(vehicle.getBrand());
		data.setModel(vehicle.getModel());
		data.setNumberPlate(vehicle.getNumberPlate());
		data.setOnRepair(vehicle.isOnRepair());
		data.setReadyToBorrow(vehicle.isReadyToBorrow());
		
		return data;
	}
	
	/**
     * Changes a VehicleJDO list to a VehicleData list.
     * 
     * @param vehicles The vehicle JDO list to change.
     * @return VehicleData list with all the necessary information.
     */
	public List<VehicleData> VehicleJDOListToData(List<VehicleJDO> vehicles){
		List<VehicleData> data = new ArrayList<VehicleData>();
    	for (VehicleJDO jdo : vehicles) {
			data.add(VehicleAssembler.getInstance().VehicleJDOToData(jdo));
		}
    	
    	return data;
	}
	
	/**
     * Changes the VehicleData to VehicleJDO.
     * 
     * @param vehicle The vehicle data to change.
     * @return VehicleJDO with all the necessary information.
     */
	public VehicleJDO VehicleDataToJDO(VehicleData vehicle) {
		VehicleJDO jdo = new VehicleJDO();		
		
		jdo.setBrand(vehicle.getBrand());
		jdo.setModel(vehicle.getModel());
		jdo.setNumberPlate(vehicle.getNumberPlate());
		jdo.setOnRepair(vehicle.isOnRepair());
		jdo.setReadyToBorrow(vehicle.isReadyToBorrow());
		
		return jdo;
	}
}
