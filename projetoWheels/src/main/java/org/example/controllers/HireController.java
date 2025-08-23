package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.dao.BikeDAO;
import org.example.dao.CustomerDAO;
import org.example.dao.HireDAO;
import org.example.models.Bike;
import org.example.models.Customer;
import org.example.models.Hire;

import java.time.LocalDate;
import java.util.List;

public class HireController {

    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<Bike> bikeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private Button confirmButton;
    @FXML private Label totalLabel;
    @FXML private Button generatePdfButton;

    private String ultimaMensagemRecibo;

    @FXML
    public void initialize() {
        carregarClientes();
        carregarBikesDisponiveis();
        configurarConversores();
        configurarDatePickers();
        configurarBotoes();

        confirmButton.setVisible(false);
        generatePdfButton.setVisible(false);
    }

    private void carregarClientes() {
        List<Customer> customers = CustomerDAO.listarTodos();
        customerComboBox.getItems().setAll(customers);
    }

    private void carregarBikesDisponiveis() {
        List<Bike> bikes = BikeDAO.listarTodos();
        bikeComboBox.getItems().setAll(bikes.stream().filter(Bike::isAvailable).toList());
    }

    //converte o combo box em string para mostrar so o nome dos clientes

    private void configurarConversores() {
        customerComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer customer) {
                return customer != null ? customer.getCustomerName() : "";
            }
            @Override
            public Customer fromString(String s) {
                return null;
            }
        });

        bikeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Bike bike) {
                if (bike == null) return "";
                return bike.getBikeBrand() + " | " + bike.getBikeModel() + " | R$" + bike.getRentalPrice();
            }
            @Override
            public Bike fromString(String s) {
                return null;
            }
        });
    }

    private void configurarDatePickers() {
        startDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(1));

        returnDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(startDatePicker.getValue().plusDays(1)));
            }
        });
    }

    private void configurarBotoes() {
        confirmButton.setOnAction(event -> handleSalvarAluguel());
        generatePdfButton.setOnAction(event -> handleGerarPdf());
    }

    @FXML
    private void handleSalvarAluguel() {
        Customer selectedCustomer = customerComboBox.getValue();
        Bike selectedBike = bikeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate returnDate = returnDatePicker.getValue();

        if (selectedCustomer == null || selectedBike == null || startDate == null || returnDate == null) {
            showAlert("por favor, preencha todos os campos.");
            return;
        }
        if (!returnDate.isAfter(startDate)) {
            showAlert("a data de devolução deve ser depois da data de início.");
            return;
        }

        int numberDays = (int) (returnDate.toEpochDay() - startDate.toEpochDay());

        Hire hire = new Hire();
        hire.setCustomer(selectedCustomer);
        hire.setBike(selectedBike);
        hire.setStartDate(startDate);
        hire.setDateReturn(returnDate);
        hire.setNumberDays(numberDays);
        hire.setLatenessDeduction(0.0);
        hire.setDamageDeduction(0.0);

        HireDAO.salvar(hire);

        selectedBike.setIsAvailable(false);
        BikeDAO.atualizar(selectedBike);

        showAlert("aluguel confirmado com sucesso!");

        limparFormulario();
        carregarBikesDisponiveis();

        generatePdfButton.setVisible(true);
        confirmButton.setVisible(false);
    }

    @FXML
    private void calcularAluguel() {
        Customer selectedCustomer = customerComboBox.getValue();
        Bike selectedBike = bikeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate returnDate = returnDatePicker.getValue();

        if (selectedCustomer == null || selectedBike == null || startDate == null || returnDate == null) {
            showAlert("selecione o cliente, a bicicleta e as datas para calcular o valor.");
            totalLabel.setText("total a pagar: R$ 0,00");
            confirmButton.setVisible(false);
            return;
        }

        if (!returnDate.isAfter(startDate)) {
            showAlert("a data de devolução deve ser depois da data de início.");
            totalLabel.setText("total a pagar: R$ 0,00");
            confirmButton.setVisible(false);
            return;
        }

        int numberDays = (int) (returnDate.toEpochDay() - startDate.toEpochDay());
        double diaria = selectedBike.getRentalPrice();

        double totalAluguel = diaria * numberDays;
        boolean desconto = selectedCustomer.getIsSenior() || selectedCustomer.getIsStudent();

        if (desconto) {
            totalAluguel *= 0.90;
        }

        double depositPrice = selectedBike.getDepositPrice();
        double totalFinal = totalAluguel + depositPrice;

        totalLabel.setText(String.format("total a pagar: R$ %.2f", totalFinal));
        confirmButton.setVisible(true);

        String mensagem = String.format("""
            ==================================
                     RECIBO DE ALUGUEL
            ==================================

            cliente: %s
            bicicleta: %s %s

            data de início: %s
            data de devolução: %s
            quantidade de dias: %d

            valor da diária: R$ %.2f
            -----------------------------------
            desconto:           %s
            subtotal:           R$ %.2f
            depósito:           R$ %.2f

            ----------------------------------
            TOTAL A PAGAR:      R$ %.2f
            ==================================
            """,
                selectedCustomer.getCustomerName(),
                selectedBike.getBikeBrand(), selectedBike.getBikeModel(),
                startDate,
                returnDate,
                numberDays,
                diaria,
                desconto ? "10% aplicado" : "sem desconto",
                totalAluguel,
                depositPrice,
                totalFinal);

        showAlert(mensagem);
        ultimaMensagemRecibo = mensagem;
    }

    private void limparFormulario() {
        customerComboBox.setValue(null);
        bikeComboBox.setValue(null);
        startDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(1));
        totalLabel.setText("total a pagar: R$ 0,00");
        confirmButton.setVisible(false);
        generatePdfButton.setVisible(false);
    }

    private void showAlert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("recibo");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    //itext pdf caso esqueça

    @FXML
    private void handleGerarPdf() {
        if (ultimaMensagemRecibo == null || ultimaMensagemRecibo.isEmpty()) {
            showAlert("nenhum recibo disponível para gerar PDF.");
            return;
        }

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            String filePath = "recibo_aluguel.pdf";
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));
            document.open();

            String logoPath = "src/main/resources/Wheels.png";
            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(logoPath);
            logo.scaleToFit(100, 100);
            logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(logo);

            document.add(com.itextpdf.text.Chunk.NEWLINE);

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("WHEELS", titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            com.itextpdf.text.pdf.draw.LineSeparator line = new com.itextpdf.text.pdf.draw.LineSeparator();
            document.add(new com.itextpdf.text.Chunk(line));
            document.add(com.itextpdf.text.Chunk.NEWLINE);

            com.itextpdf.text.Font bodyFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 14);

            String[] linhas = ultimaMensagemRecibo.split("\n");
            for (String linha : linhas) {
                com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph(linha.trim(), bodyFont);
                p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                p.setSpacingAfter(5f);
                document.add(p);
            }

            document.close();

            showAlert("PDF gerado com sucesso em: " + filePath);
            enviarPdfParaTelegram(filePath);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("erro ao gerar o PDF: " + e.getMessage());
        }
    }


    //botFather no telegram com bots

    private void enviarPdfParaTelegram(String filePath) {
        String botToken = "7563276216:AAENZzvRDW6SESh932Cdf3pBeedNYCkJIqA";
        String chatId = "5916109417";

        try {
            java.net.URL url = new java.net.URL("https://api.telegram.org/bot" + botToken + "/sendDocument");

            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            java.io.OutputStream outputStream = conn.getOutputStream();
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(outputStream, "UTF-8"), true);

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n");
            writer.append(chatId).append("\r\n");

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"caption\"\r\n\r\n");
            writer.append("Segue em anexo o seu recibo de aluguel da loja Wheels!\n" +
                    "Agradecemos por alugar conosco! 🚴🚴‍♂️😁\n" +
                    "\n" +
                    "Lembre-se de devolver a bicicleta até a data estipulada para devolução — ou antes — para evitar cobranças adicionais! 👀👀\n" +
                    "\n" +
                    "Estamos à disposição para o que precisar. Boa pedalada! 😎🚴‍♂️🚴").append("\r\n");

            java.io.File file = new java.io.File(filePath);
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"document\"; filename=\"").append(file.getName()).append("\"\r\n");
            writer.append("Content-Type: application/pdf\r\n\r\n");
            writer.flush();

            java.nio.file.Files.copy(file.toPath(), outputStream);
            outputStream.flush();

            writer.append("\r\n");
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                showAlert("PDF enviado para o telegram com sucesso!");
            } else {
                showAlert("Falha ao enviar PDF para o Telegram. Código: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("erro ao enviar para o telegram: " + e.getMessage());
        }
    }
}
