package org.example.models;

import javafx.beans.property.*;

public class Customer {
    private final IntegerProperty customerId = new SimpleIntegerProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty customerAddress = new SimpleStringProperty();
    private final StringProperty customerTelephoneNumber = new SimpleStringProperty();
    private final BooleanProperty isStudent = new SimpleBooleanProperty();
    private final BooleanProperty isSenior = new SimpleBooleanProperty();

    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int id) {
        this.customerId.set(id);
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress.get();
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress.set(customerAddress);
    }

    public StringProperty customerAddressProperty() {
        return customerAddress;
    }

    public String getCustomerTelephoneNumber() {
        return customerTelephoneNumber.get();
    }

    public void setCustomerTelephoneNumber(String customerTelephoneNumber) {
        this.customerTelephoneNumber.set(customerTelephoneNumber);
    }

    public StringProperty customerTelephoneNumberProperty() {
        return customerTelephoneNumber;
    }

    public boolean getIsStudent() {
        return isStudent.get();
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent.set(isStudent);
    }

    public BooleanProperty isStudentProperty() {
        return isStudent;
    }

    public boolean getIsSenior() {
        return isSenior.get();
    }

    public void setIsSenior(boolean isSenior) {
        this.isSenior.set(isSenior);
    }

    public BooleanProperty isSeniorProperty() {
        return isSenior;
    }
}
