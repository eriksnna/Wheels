package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.dao.BikeDAO;
import org.example.models.Bike;

import java.util.Locale;

public class BikeController {

    @FXML private TextField bikeTypeField;
    @FXML private TextField bikeBrandField;
    @FXML private TextField bikeModelField;
    @FXML private TextField rentalPriceField;
    @FXML private TextField depositPriceField;

    //lida com o funcionamento do botao de salvar (apresentacao)

    // preco do deposito: diaria * 3
    // preco de atraso: diaria * 0.5

    @FXML
    private void handleSalvar() {
        if (!todosCamposPreenchidos()) {
            showAlert(Alert.AlertType.WARNING, "aviso", "todos os campos são obrigatórios!");
            return;
        }

        Double preco = parseDouble(rentalPriceField.getText(), "erro", "preço inválido! insira um número válido.");
        if (preco == null) return;

        Double deposito = parseDouble(depositPriceField.getText(), "erro", "depósito inválido! insira um número válido.");
        if (deposito == null) return;

        Bike bike = new Bike();
        bike.setBikeType(bikeTypeField.getText());
        bike.setBikeBrand(bikeBrandField.getText());
        bike.setBikeModel(bikeModelField.getText());
        bike.setRentalPrice(preco);
        bike.setDepositPrice(deposito);
        bike.setIsAvailable(true);

        BikeDAO.salvar(bike);
        limparCampos();
        showAlert(Alert.AlertType.INFORMATION, "sucesso", "bicicleta salva com sucesso!");
    }

    // checa aqui se os campos estao preenchidos (apresentacao)

    private boolean todosCamposPreenchidos() {
        return !bikeTypeField.getText().isEmpty() &&
                !bikeBrandField.getText().isEmpty() &&
                !bikeModelField.getText().isEmpty() &&
                !rentalPriceField.getText().isEmpty() &&
                !depositPriceField.getText().isEmpty();
    }

    private Double parseDouble(String texto, String tipoAlerta, String mensagem) {
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.valueOf(tipoAlerta.toUpperCase()), tipoAlerta, mensagem);
            return null;
        }
    }

    private void limparCampos() {
        bikeTypeField.clear();
        bikeBrandField.clear();
        bikeModelField.clear();
        rentalPriceField.clear();
        depositPriceField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void atualizarDeposito() {
        String precoText = rentalPriceField.getText();
        if (precoText == null || precoText.isEmpty()) {
            depositPriceField.clear();
            return;
        }

        try {
            double preco = Double.parseDouble(precoText);
            double deposito = calcularDeposito(preco);
            depositPriceField.setText(String.format(Locale.US, "%.2f", deposito));
        } catch (NumberFormatException e) {
            depositPriceField.clear();
        }
    }

    private double calcularDeposito(double rentalPrice) {
        return rentalPrice * 3;
    }
}
