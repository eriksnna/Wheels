package org.example.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Payment {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> paymentDate = new SimpleObjectProperty<>();
    private final DoubleProperty totalAmountPaid = new SimpleDoubleProperty();
    private final DoubleProperty totalDepositPaid = new SimpleDoubleProperty();
    private final DoubleProperty totalDepositReturned = new SimpleDoubleProperty();
    private final BooleanProperty paid = new SimpleBooleanProperty();
    private final IntegerProperty bikeId = new SimpleIntegerProperty();
    private final StringProperty customerName = new SimpleStringProperty();

    private Customer customer;

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public LocalDate getPaymentDate() {
        return paymentDate.get();
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate.set(paymentDate);
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid.get();
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid.set(totalAmountPaid);
    }

    public double getTotalDepositPaid() {
        return totalDepositPaid.get();
    }

    public void setTotalDepositPaid(double totalDepositPaid) {
        this.totalDepositPaid.set(totalDepositPaid);
    }

    public double getTotalDepositReturned() {
        return totalDepositReturned.get();
    }

    public void setTotalDepositReturned(double totalDepositReturned) {
        this.totalDepositReturned.set(totalDepositReturned);
    }

    public boolean isPaid() {
        return paid.get();
    }

    public void setPaid(boolean paid) {
        this.paid.set(paid);
    }

    public BooleanProperty paidProperty() {
        return paid;
    }

    public int getBikeId() {
        return bikeId.get();
    }

    public void setBikeId(int bikeId) {
        this.bikeId.set(bikeId);
    }

    public IntegerProperty bikeIdProperty() {
        return bikeId;
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
}
