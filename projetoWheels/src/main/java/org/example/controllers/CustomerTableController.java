package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import org.example.dao.CustomerDAO;
import org.example.models.Customer;

public class CustomerTableController {

    @FXML private TableView<Customer> tableView;
    @FXML private TableColumn<Customer, Number> colCustomerId;
    @FXML private TableColumn<Customer, String> colCustomerName;
    @FXML private TableColumn<Customer, String> colCustomerAddress;
    @FXML private TableColumn<Customer, String> colCustomerTelephoneNumber;
    @FXML private TableColumn<Customer, Boolean> colIsStudent;
    @FXML private TableColumn<Customer, Boolean> colIsSenior;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tableView.setEditable(true);

        configurarColunaCustomerId();
        configurarColunaEditavelNome();
        configurarColunaEditavelEndereco();
        configurarColunaEditavelTelefone();

        configurarColunaCheckboxIsStudent();
        configurarColunaCheckboxIsSenior();

        customers.setAll(CustomerDAO.listarTodos());
        tableView.setItems(customers);
    }

    private void configurarColunaCustomerId() {
        colCustomerId.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        colCustomerId.setEditable(false);
    }

    private void configurarColunaEditavelNome() {
        colCustomerName.setEditable(true);
        colCustomerName.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        colCustomerName.setCellFactory(TextFieldTableCell.forTableColumn());
        colCustomerName.setOnEditCommit(e -> {
            System.out.println("nome alterado para: " + e.getNewValue());
            e.getRowValue().setCustomerName(e.getNewValue());
        });
    }

    private void configurarColunaEditavelEndereco() {
        colCustomerAddress.setEditable(true);
        colCustomerAddress.setCellValueFactory(cellData -> cellData.getValue().customerAddressProperty());
        colCustomerAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        colCustomerAddress.setOnEditCommit(e -> {
            System.out.println("endereço alterado para: " + e.getNewValue());
            e.getRowValue().setCustomerAddress(e.getNewValue());
        });
    }

    private void configurarColunaEditavelTelefone() {
        colCustomerTelephoneNumber.setEditable(true);
        colCustomerTelephoneNumber.setCellValueFactory(cellData -> cellData.getValue().customerTelephoneNumberProperty());
        colCustomerTelephoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        colCustomerTelephoneNumber.setOnEditCommit(e -> {
            System.out.println("número de telefone alterado para: " + e.getNewValue());
            e.getRowValue().setCustomerTelephoneNumber(e.getNewValue());
        });
    }

    private void configurarColunaCheckboxIsStudent() {
        colIsStudent.setCellValueFactory(cellData -> cellData.getValue().isStudentProperty());
        colIsStudent.setCellFactory(CheckBoxTableCell.forTableColumn(colIsStudent));
        colIsStudent.setEditable(true);
    }

    private void configurarColunaCheckboxIsSenior() {
        colIsSenior.setCellValueFactory(cellData -> cellData.getValue().isSeniorProperty());
        colIsSenior.setCellFactory(CheckBoxTableCell.forTableColumn(colIsSenior));
        colIsSenior.setEditable(true);
    }

    @FXML
    private void handleSalvarAlteracoes() {
        for (Customer c : customers) {
            CustomerDAO.atualizar(c);
        }

        customers.setAll(CustomerDAO.listarTodos());
        tableView.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("sucesso!!");
        alert.setHeaderText(null);
        alert.setContentText("alterações atualizadas com sucesso!");
        alert.showAndWait();
    }
}
