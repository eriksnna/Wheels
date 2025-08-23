package org.example.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Hire {

    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private final IntegerProperty numberDays = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateReturn = new SimpleObjectProperty<>();
    private final DoubleProperty latenessDeduction = new SimpleDoubleProperty();
    private final DoubleProperty damageDeduction = new SimpleDoubleProperty();
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final BooleanProperty paymentMade = new SimpleBooleanProperty();

    private final ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private final ObjectProperty<Bike> bike = new SimpleObjectProperty<>();

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public int getNumberDays() {
        return numberDays.get();
    }

    public void setNumberDays(int numberDays) {
        this.numberDays.set(numberDays);
    }

    public LocalDate getDateReturn() {
        return dateReturn.get();
    }

    public void setDateReturn(LocalDate dateReturn) {
        this.dateReturn.set(dateReturn);
    }

    public Double getLatenessDeduction() {
        return latenessDeduction.get();
    }

    public void setLatenessDeduction(Double latenessDeduction) {
        this.latenessDeduction.set(latenessDeduction);
    }

    public Double getDamageDeduction() {
        return damageDeduction.get();
    }

    public void setDamageDeduction(Double damageDeduction) {
        this.damageDeduction.set(damageDeduction);
    }

    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Customer customer) {
        this.customer.set(customer);
    }

    public Boolean getPaymentMade() {
        return paymentMade.get();
    }

    public void setPaymentMade(Boolean paymentMade) {
        this.paymentMade.set(paymentMade);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Bike getBike() {
        return bike.get();
    }

    public void setBike(Bike bike) {
        this.bike.set(bike);
    }
}
