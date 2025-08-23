package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.models.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public static void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_name TEXT,
                bike_id INTEGER,
                payment_date TEXT NOT NULL,
                total_amount_paid REAL NOT NULL,
                total_deposit_paid REAL NOT NULL,
                total_deposit_returned REAL NOT NULL,
                is_paid INTEGER NOT NULL DEFAULT 0
            );
        """;

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void salvar(Payment payment) {
        String sql = """
            INSERT INTO payments (
                customer_name,
                bike_id,
                payment_date,
                total_amount_paid,
                total_deposit_paid,
                total_deposit_returned,
                is_paid
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, payment.getCustomerName());
            stmt.setInt(2, payment.getBikeId());
            stmt.setString(3, payment.getPaymentDate().toString());
            stmt.setDouble(4, payment.getTotalAmountPaid());
            stmt.setDouble(5, payment.getTotalDepositPaid());
            stmt.setDouble(6, payment.getTotalDepositReturned());
            stmt.setInt(7, payment.isPaid() ? 1 : 0);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar o pagamento: " + e.getMessage());
        }
    }

    public static List<Payment> listar() {
        List<Payment> lista = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setCustomerName(rs.getString("customer_name"));
                payment.setBikeId(rs.getInt("bike_id"));
                payment.setPaymentDate(LocalDate.parse(rs.getString("payment_date")));
                payment.setTotalAmountPaid(rs.getDouble("total_amount_paid"));
                payment.setTotalDepositPaid(rs.getDouble("total_deposit_paid"));
                payment.setTotalDepositReturned(rs.getDouble("total_deposit_returned"));
                payment.setPaid(rs.getInt("is_paid") == 1);

                lista.add(payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar os pagamentos: " + e.getMessage());
        }

        return lista;
    }
}
