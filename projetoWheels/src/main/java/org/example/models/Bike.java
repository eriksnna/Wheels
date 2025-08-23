package org.example.models;

import javafx.beans.property.*;

public class Bike {
    private final IntegerProperty bikeId = new SimpleIntegerProperty();
    private final StringProperty bikeType = new SimpleStringProperty();
    private final StringProperty bikeBrand = new SimpleStringProperty();
    private final StringProperty bikeModel = new SimpleStringProperty();
    private final DoubleProperty rentalPrice = new SimpleDoubleProperty();
    private final DoubleProperty depositPrice = new SimpleDoubleProperty();
    private final BooleanProperty isAvailable = new SimpleBooleanProperty();

    public int getBikeId() {
        return bikeId.get();
    }

    public void setBikeId(int bikeId) {
        this.bikeId.set(bikeId);
    }

    public IntegerProperty bikeIdProperty() {
        return bikeId;
    }

    public String getBikeType() {
        return bikeType.get();
    }

    public void setBikeType(String bikeType) {
        this.bikeType.set(bikeType);
    }

    public StringProperty bikeTypeProperty() {
        return bikeType;
    }

    public String getBikeBrand() {
        return bikeBrand.get();
    }

    public void setBikeBrand(String bikeBrand) {
        this.bikeBrand.set(bikeBrand);
    }

    public StringProperty bikeBrandProperty() {
        return bikeBrand;
    }

    public String getBikeModel() {
        return bikeModel.get();
    }

    public void setBikeModel(String bikeModel) {
        this.bikeModel.set(bikeModel);
    }

    public StringProperty bikeModelProperty() {
        return bikeModel;
    }

    public double getRentalPrice() {
        return rentalPrice.get();
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice.set(rentalPrice);
    }

    public DoubleProperty rentalPriceProperty() {
        return rentalPrice;
    }

    public double getDepositPrice() {
        return depositPrice.get();
    }

    public void setDepositPrice(double depositPrice) {
        this.depositPrice.set(depositPrice);
    }

    public DoubleProperty depositPriceProperty() {
        return depositPrice;
    }

    public boolean isAvailable() {
        return isAvailable.get();
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable.set(isAvailable);
    }

    public BooleanProperty isAvailableProperty() {
        return isAvailable;
    }
}
