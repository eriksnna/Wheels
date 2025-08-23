package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.dao.BikeDAO;
import org.example.dao.HireDAO;
import org.example.dao.PaymentDAO;
import org.example.models.Bike;
import org.example.models.Hire;
import org.example.models.Payment;

import java.time.LocalDate;

public class PaymentController {

    @FXML
    private ComboBox<Hire> hireComboBox;

    @FXML
    private DatePicker paymentDatePicker;

    @FXML
    private CheckBox damagedCheckBox;

    @FXML
    private Label totalPaymentLabel;

    @FXML
    private Label totalPaymentValueLabel;

    @FXML
    private Label depositReturnLabel;

    @FXML
    private Button confirmPaymentButton;

    private Payment currentPayment;

    @FXML
    public void initialize() {
        hireComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Hire hire) {
                if (hire == null || hire.getBike() == null) return "";
                return String.format("%s | bike id: #%d | %s %s %s",
                        hire.getCustomer().getCustomerName(),
                        hire.getBike().getBikeId(),
                        hire.getBike().getBikeBrand(),
                        hire.getBike().getBikeType(),
                        hire.getBike().getBikeModel());
            }

            @Override
            public Hire fromString(String string) {
                return null;
            }
        });

        carregarAlugueisPendentes();

        paymentDatePicker.setValue(LocalDate.now());

        totalPaymentLabel.setVisible(true);
        totalPaymentValueLabel.setVisible(true);
        depositReturnLabel.setVisible(true);

        hireComboBox.setOnAction(e -> calcularPagamento());
        paymentDatePicker.setOnAction(e -> calcularPagamento());
        damagedCheckBox.setOnAction(e -> calcularPagamento());

        confirmPaymentButton.setOnAction(e -> handleConfirmarPagmento());
    }

    //carrega os alugueis que nao foram pagos ainda

    private void carregarAlugueisPendentes() {
        hireComboBox.getItems().clear();
        hireComboBox.getItems().addAll(HireDAO.listarTodosPendentes());
    }

    private void calcularPagamento() {
        Hire selectedHire = hireComboBox.getValue();
        LocalDate paymentDate = paymentDatePicker.getValue();
        boolean isDamaged = damagedCheckBox.isSelected();

        if (selectedHire == null || paymentDate == null) {
            totalPaymentValueLabel.setText("R$ 0,00");
            depositReturnLabel.setText("R$ 0,00");
            currentPayment = null;
            return;
        }

        double dailyPrice = selectedHire.getBike().getRentalPrice();
        double depositPrice = selectedHire.getBike().getDepositPrice();

        double latenessFee = 0;
        if (paymentDate.isAfter(selectedHire.getDateReturn())) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(selectedHire.getDateReturn(), paymentDate);
            latenessFee = daysLate * (dailyPrice * 0.5);
        }

        double damageFee = isDamaged ? dailyPrice * 2 : 0;

        double extraCosts = latenessFee + damageFee;

        double totalToPay;
        double depositReturn;

        if (extraCosts <= depositPrice) {
            totalToPay = 0;
            depositReturn = depositPrice - extraCosts;
        } else {
            totalToPay = extraCosts - depositPrice;
            depositReturn = 0;
        }

        totalPaymentValueLabel.setText(String.format("R$ %.2f", totalToPay));
        depositReturnLabel.setText(String.format("R$ %.2f", depositReturn));

        if (currentPayment == null) {
            currentPayment = new Payment();
        }
        currentPayment.setPaymentDate(paymentDate);
        currentPayment.setTotalAmountPaid(totalToPay);
        currentPayment.setTotalDepositPaid(depositPrice);
        currentPayment.setTotalDepositReturned(depositReturn);
    }

    @FXML
    private void handleConfirmarPagmento() {
        if (hireComboBox.getValue() == null) {
            showAlert("selecione um aluguel para efetuar a devolução.");
            return;
        }

        if (currentPayment == null) {
            showAlert("calcule o pagamento antes de confirmar.");
            return;
        }

        try {
            Hire hire = hireComboBox.getValue();

            currentPayment.setCustomerName(hire.getCustomer().getCustomerName());
            currentPayment.setBikeId(hire.getBike().getBikeId());

            currentPayment.setPaid(true);
            PaymentDAO.salvar(currentPayment);

            hire.setPaymentMade(true);
            HireDAO.atualizar(hire);

            Bike bike = hire.getBike();


            if (!damagedCheckBox.isSelected()) {
                bike.setIsAvailable(true);
                BikeDAO.atualizar(bike);
            } else {
                showAlert("atenção: a bicicleta foi marcada como danificada e não está disponível para novos aluguéis.");
            }


            showAlert("devolução confirmada com sucesso!");

            carregarAlugueisPendentes();

            currentPayment = null;

            limparFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("erro ao confirmar pagamento: " + e.getMessage());
        }
    }


    private void limparFormulario() {
        hireComboBox.setValue(null);
        paymentDatePicker.setValue(LocalDate.now());
        damagedCheckBox.setSelected(false);

        totalPaymentValueLabel.setText("R$ 0,00");
        depositReturnLabel.setText("R$ 0,00");

        currentPayment = null;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("devolução");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
