package es.deusto.spq.serialization;

/**
 * Represents a vehicle.
 */
public class Vehicle {
    private String numberPlate;
    private String brand;
    private String model;
    private boolean readyToBorrow;
    private boolean onRepair;
    
    /**
     * Constructs a Vehicle object with the specified number plate, brand, and model.
     * By default, the vehicle is ready to borrow and not on repair.
     * 
     * @param numberPlate The number plate of the vehicle.
     * @param brand       The brand of the vehicle.
     * @param model       The model of the vehicle.
     */
    public Vehicle(String numberPlate, String brand, String model) {
        this.numberPlate = numberPlate;
        this.brand = brand;
        this.model = model;
        this.readyToBorrow = true;
        this.onRepair = false;
    }
    
    /**
     * Retrieves the number plate of the vehicle.
     * 
     * @return The number plate of the vehicle.
     */
    public String getNumberPlate() {
        return numberPlate;
    }

    /**
     * Sets the number plate of the vehicle.
     * 
     * @param numberPlate The number plate to set.
     */
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    /**
     * Retrieves the brand of the vehicle.
     * 
     * @return The brand of the vehicle.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the vehicle.
     * 
     * @param brand The brand to set.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Retrieves the model of the vehicle.
     * 
     * @return The model of the vehicle.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the vehicle.
     * 
     * @param model The model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Checks if the vehicle is ready to borrow.
     * 
     * @return True if the vehicle is ready to borrow, otherwise false.
     */
    public boolean isReadyToBorrow() {
        return readyToBorrow;
    }

    /**
     * Sets the status of the vehicle whether it is ready to borrow.
     * 
     * @param readyToBorrow The status to set.
     */
    public void setReadyToBorrow(boolean readyToBorrow) {
        this.readyToBorrow = readyToBorrow;
    }

    /**
     * Checks if the vehicle is on repair.
     * 
     * @return True if the vehicle is on repair, otherwise false.
     */
    public boolean isOnRepair() {
        return onRepair;
    }

    /**
     * Sets the status of the vehicle whether it is on repair.
     * 
     * @param onRepair The status to set.
     */
    public void setOnRepair(boolean onRepair) {
        this.onRepair = onRepair;
    }
    
    /**
     * Returns a string representation of the Vehicle object.
     * 
     * @return A string representation of the Vehicle object.
     */
    @Override
    public String toString() {
        return "Vehicle{" +
               "numberPlate='" + numberPlate + '\'' +
               ", brand='" + brand + '\'' +
               ", model='" + model + '\'' +
               ", readyToBorrow=" + readyToBorrow +
               ", onRepair=" + onRepair +
               '}';
    }
}
