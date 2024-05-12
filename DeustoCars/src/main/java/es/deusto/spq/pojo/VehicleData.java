package es.deusto.spq.pojo;

/**
 * Represents a vehicle.
 */
public class VehicleData {
    private String numberPlate;
    private String brand;
    private String model;
    private boolean readyToBorrow;
    private boolean onRepair;
    
    /**
     * Default constructor required for serialization.
     */
    public VehicleData(){
        // Required for serialization
    }

    /**
     * Constructs a VehicleData object with the specified number plate, brand, and model.
     *
     * @param numberPlate The number plate of the vehicle.
     * @param brand       The brand of the vehicle.
     * @param model       The model of the vehicle.
     */
    public VehicleData(String numberPlate, String brand,  String model, boolean borrow, boolean repair) {
        this.numberPlate = numberPlate;
        this.brand = brand;
        this.model = model;
        this.readyToBorrow = borrow;
        this.onRepair = repair;
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
     * @return True if the vehicle is ready to borrow, false otherwise.
     */
    public boolean isReadyToBorrow() {
        return readyToBorrow;
    }

    /**
     * Sets whether the vehicle is ready to borrow.
     *
     * @param readyToBorrow True if the vehicle is ready to borrow, false otherwise.
     */
    public void setReadyToBorrow(boolean readyToBorrow) {
        this.readyToBorrow = readyToBorrow;
    }

    /**
     * Checks if the vehicle is on repair.
     *
     * @return True if the vehicle is on repair, false otherwise.
     */
    public boolean isOnRepair() {
        return onRepair;
    }

    /**
     * Sets whether the vehicle is on repair.
     *
     * @param onRepair True if the vehicle is on repair, false otherwise.
     */
    public void setOnRepair(boolean onRepair) {
        this.onRepair = onRepair;
    }
    
    /**
     * Returns a string representation of the VehicleData object.
     *
     * @return A string representation of the VehicleData object.
     */
    @Override
    public String toString() {
        return "VehicleData{" +
               "\nNumber Plate: " + numberPlate +
               "\nBrand: " + brand +
               "\nModel: " + model +
               "\nReady to Borrow: " + readyToBorrow +
               "\nOn Repair: " + onRepair +
               '}';
    }
}
