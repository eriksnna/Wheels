package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import org.example.dao.BikeDAO;
import org.example.models.Bike;

public class BikeTableController {

    @FXML private TableView<Bike> tableView;
    @FXML private TableColumn<Bike, Number> colBikeId;
    @FXML private TableColumn<Bike, String> colBikeType;
    @FXML private TableColumn<Bike, String> colBikeBrand;
    @FXML private TableColumn<Bike, String> colBikeModel;
    @FXML private TableColumn<Bike, Number> colRentalPrice;
    @FXML private TableColumn<Bike, Number> colDepositPrice;
    @FXML private TableColumn<Bike, Boolean> colIsAvailable;

    private final ObservableList<Bike> bikes = FXCollections.observableArrayList();

    //inicializa as tabelas (apresentacao)

    @FXML
    private void initialize() {
        tableView.setEditable(true);
        configurarColunasEditaveis();
        configurarColunasNaoEditaveis();
        carregarBikes();
    }

    private void configurarColunasEditaveis() {
        configurarColunaTexto(colBikeType, "tipo");
        configurarColunaTexto(colBikeBrand, "marca");
        configurarColunaTexto(colBikeModel, "modelo");
        configurarColunaNumero(colRentalPrice, "preço");
        configurarColunaNumero(colDepositPrice, "depósito");
    }

    private void configurarColunaTexto(TableColumn<Bike, String> coluna, String nomeCampo) {
        coluna.setEditable(true);
        coluna.setCellValueFactory(cellData -> {
            switch (nomeCampo) {
                case "tipo": return cellData.getValue().bikeTypeProperty();
                case "marca": return cellData.getValue().bikeBrandProperty();
                case "modelo": return cellData.getValue().bikeModelProperty();
                default: return null;
            }
        });
        coluna.setCellFactory(TextFieldTableCell.forTableColumn());
        coluna.setOnEditCommit(e -> {
            System.out.println(nomeCampo + " alterado para: " + e.getNewValue());
            switch (nomeCampo) {
                case "tipo": e.getRowValue().setBikeType(e.getNewValue()); break;
                case "marca": e.getRowValue().setBikeBrand(e.getNewValue()); break;
                case "modelo": e.getRowValue().setBikeModel(e.getNewValue()); break;
            }
        });
    }

    private void configurarColunaNumero(TableColumn<Bike, Number> coluna, String nomeCampo) {
        coluna.setEditable(true);
        coluna.setCellValueFactory(cellData -> {
            switch (nomeCampo) {
                case "preço": return cellData.getValue().rentalPriceProperty();
                case "depósito": return cellData.getValue().depositPriceProperty();
                default: return null;
            }
        });
        coluna.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        coluna.setOnEditCommit(e -> {
            System.out.println(nomeCampo + " alterado para: " + e.getNewValue());
            switch (nomeCampo) {
                case "preço": e.getRowValue().setRentalPrice(e.getNewValue().doubleValue()); break;
                case "depósito": e.getRowValue().setDepositPrice(e.getNewValue().doubleValue()); break;
            }
        });
    }

    private void configurarColunasNaoEditaveis() {
        colBikeId.setCellValueFactory(cellData -> cellData.getValue().bikeIdProperty());

        colIsAvailable.setCellValueFactory(cellData -> cellData.getValue().isAvailableProperty());
        colIsAvailable.setCellFactory(CheckBoxTableCell.forTableColumn(colIsAvailable));
    }

    private void carregarBikes() {
        bikes.setAll(BikeDAO.listarTodos());
        tableView.setItems(bikes);
    }


    @FXML
    private void handleSalvarAlteracoes() {
        for (Bike bike : bikes) {
            BikeDAO.atualizar(bike);
        }
        bikes.setAll(BikeDAO.listarTodos());
        tableView.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("salvo!");
        alert.setHeaderText(null);
        alert.setContentText("alterações atualizadas com sucesso!");
        alert.showAndWait();
    }

}
