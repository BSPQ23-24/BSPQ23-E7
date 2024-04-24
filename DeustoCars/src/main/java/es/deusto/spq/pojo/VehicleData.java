package es.deusto.spq.pojo;

/**
 * Class that represents a vehicle.
 */

public class VehicleData {
    private String numberPlate;
    private String brand;
    private String model;
    private boolean readyToBorrow;
    private boolean onRepair;
    
    /**
     * Vehicle class constructor.
     * 
     */
    
    
    public VehicleData(String numberPlate, String brand,  String model) {
        this.numberPlate = numberPlate;
        this.brand = brand;
        this.model = model;
        this.readyToBorrow = true;
        this.onRepair = false;
    }

    public VehicleData(){
        // Required for serialization
    }


    /**
     * Getter and setter methods of the Vehicle class.
     * 
     */

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }



    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isReadyToBorrow() {
        return readyToBorrow;
    }

    public void setReadyToBorrow(boolean readyToBorrow) {
        this.readyToBorrow = readyToBorrow;
    }

    public boolean isOnRepair() {
        return onRepair;
    }

    public void setOnRepair(boolean onRepair) {
        this.onRepair = onRepair;
    }
    
    /**
     * Returns a string representation of the Vehicle object.
     * 
     */
    
    public String toString() {
        return "Vehicle" + 
               "\nNumber Plate: " + numberPlate + 
               "\nBrand: " + brand + 
               "\nModel: " + model + 
               "\nReady to Borrow: " + readyToBorrow + 
               "\nOn Repair: " + onRepair;
    }
}

