package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void handleCadastrarCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/customer-controller.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("cadastrar cliente");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListarClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/customer-table.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("lista de clientes");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastrarBicicleta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/bike-controller.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("cadastrar bicicleta");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListarBicicletas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/bike-table.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("lista de bicicletas");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAlugarBicicleta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/hire-controller.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("alugar bicicleta");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDevolverBicicleta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/payment-controller.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("pagamento de aluguel");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHistorico() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetoWheels/payments-table.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("histórico de pagamentos");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
