package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.example.dao.CustomerDAO;
import org.example.models.Customer;

public class CustomerController {
    @FXML private TextField customerNameField;
    @FXML private TextField customerAddressField;
    @FXML private TextField customerTelephoneNumberField;
    @FXML private CheckBox isStudentCheck;
    @FXML private CheckBox isSeniorCheck;

    private Customer customer;

    @FXML
    private void initialize() {
        customer = new Customer();
        bindCampos();
    }

    @FXML
    private void handleSalvar() {
        if (camposObrigatoriosVazios()) {
            mostrarAlerta(Alert.AlertType.WARNING, "aviso", "todos os campos são obrigatórios!");
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        dao.salvar(customer);

        mostrarAlerta(Alert.AlertType.INFORMATION, "cliente cadastrado!",
                "cliente '" + customer.getCustomerName() + "' salvo com sucesso!");

        limparCampos();
    }

    private boolean camposObrigatoriosVazios() {
        return customer.getCustomerName().isEmpty() ||
                customer.getCustomerAddress().isEmpty() ||
                customer.getCustomerTelephoneNumber().isEmpty();
    }

    private void limparCampos() {
        customerNameField.clear();
        customerAddressField.clear();
        customerTelephoneNumberField.clear();
        isStudentCheck.setSelected(false);
        isSeniorCheck.setSelected(false);

        customer = new Customer();
        bindCampos();
    }

    //vincula os campos do JAVAFX com as propriedades do customer (apresentacao)

    private void bindCampos() {
        customerNameField.textProperty().bindBidirectional(customer.customerNameProperty());
        customerAddressField.textProperty().bindBidirectional(customer.customerAddressProperty());
        customerTelephoneNumberField.textProperty().bindBidirectional(customer.customerTelephoneNumberProperty());
        isStudentCheck.selectedProperty().bindBidirectional(customer.isStudentProperty());
        isSeniorCheck.selectedProperty().bindBidirectional(customer.isSeniorProperty());
    }

    private void mostrarAlerta(Alert.AlertType type, String titulo, String conteudo) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
