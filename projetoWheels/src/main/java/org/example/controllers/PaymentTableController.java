package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.PaymentDAO;
import org.example.models.Payment;

import java.time.LocalDate;

public class PaymentTableController {

    @FXML
    private TableView<Payment> tableView;

    @FXML
    private TableColumn<Payment, Integer> colId;

    @FXML
    private TableColumn<Payment, LocalDate> colPaymentDate;

    @FXML
    private TableColumn<Payment, Double> colTotalAmountPaid;

    @FXML
    private TableColumn<Payment, Double> colTotalDepositReturned;

    @FXML
    private TableColumn<Payment, String> customerNameColumn;

    @FXML
    private TableColumn<Payment, Number> bikeIdColumn;

    private final ObservableList<Payment> payments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colTotalAmountPaid.setCellValueFactory(new PropertyValueFactory<>("totalAmountPaid"));
        colTotalDepositReturned.setCellValueFactory(new PropertyValueFactory<>("totalDepositReturned"));

        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        bikeIdColumn.setCellValueFactory(cellData -> cellData.getValue().bikeIdProperty());

        atualizarTabela();
    }

    @FXML
    private void handleAtualizarLista() {
        atualizarTabela();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("sucesso!!");
        alert.setHeaderText(null);
        alert.setContentText("alterações atualizadas com sucesso!");
        alert.showAndWait();
    }

    private void atualizarTabela() {
        payments.setAll(PaymentDAO.listar());
        tableView.setItems(payments);
    }
}
