package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.models.Customer;
import org.example.models.Hire;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HireDAO {

    public static void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS hires (
                hire_id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_id INTEGER NOT NULL,
                bike_id INTEGER NOT NULL,
                start_date TEXT NOT NULL,
                number_days INTEGER NOT NULL,
                date_return TEXT NOT NULL,
                lateness_deduction REAL,
                damage_deduction REAL,
                payment_made BOOLEAN DEFAULT 0,
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
                FOREIGN KEY (bike_id) REFERENCES bikes(bike_id)
            );
        """;

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void salvar(Hire hire) {
        String sql = """
            INSERT INTO hires (
                customer_id,
                bike_id,
                start_date,
                number_days,
                date_return,
                lateness_deduction,
                damage_deduction
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hire.getCustomer().getCustomerId());
            stmt.setInt(2, hire.getBike().getBikeId());
            stmt.setString(3, hire.getStartDate().toString());
            stmt.setInt(4, hire.getNumberDays());
            stmt.setString(5, hire.getDateReturn().toString());

            if (hire.getLatenessDeduction() != null) {
                stmt.setDouble(6, hire.getLatenessDeduction());
            } else {
                stmt.setNull(6, Types.REAL);
            }

            if (hire.getDamageDeduction() != null) {
                stmt.setDouble(7, hire.getDamageDeduction());
            } else {
                stmt.setNull(7, Types.REAL);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Hire> listarTodosPendentes() {
        List<Hire> lista = new ArrayList<>();
        String sql = """
            SELECT h.*, c.customer_name
            FROM hires h
            JOIN customers c ON h.customer_id = c.customer_id
            WHERE h.date_return >= ? AND (h.payment_made IS NULL OR h.payment_made = 0)
        """;

        LocalDate hoje = LocalDate.now();

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoje.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Hire hire = new Hire();

                    hire.setId(rs.getInt("hire_id"));

                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    hire.setCustomer(customer);

                    int customerId = rs.getInt("customer_id");
                    int bikeId = rs.getInt("bike_id");

                    hire.setCustomer(CustomerDAO.buscarPorId(customerId));
                    hire.setBike(BikeDAO.buscarPorId(bikeId));

                    hire.setStartDate(LocalDate.parse(rs.getString("start_date")));
                    hire.setNumberDays(rs.getInt("number_days"));
                    hire.setDateReturn(LocalDate.parse(rs.getString("date_return")));

                    double lateness = rs.getDouble("lateness_deduction");
                    if (rs.wasNull()) {
                        hire.setLatenessDeduction(null);
                    } else {
                        hire.setLatenessDeduction(lateness);
                    }

                    double damage = rs.getDouble("damage_deduction");
                    if (rs.wasNull()) {
                        hire.setDamageDeduction(null);
                    } else {
                        hire.setDamageDeduction(damage);
                    }

                    hire.setPaymentMade(rs.getBoolean("payment_made"));

                    lista.add(hire);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void atualizar(Hire hire) {
        String sql = """
            UPDATE hires
            SET customer_id = ?,
                bike_id = ?,
                start_date = ?,
                number_days = ?,
                date_return = ?,
                lateness_deduction = ?,
                damage_deduction = ?,
                payment_made = ?
            WHERE hire_id = ?
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hire.getCustomer().getCustomerId());
            pstmt.setInt(2, hire.getBike().getBikeId());
            pstmt.setString(3, hire.getStartDate().toString());
            pstmt.setInt(4, hire.getNumberDays());
            pstmt.setString(5, hire.getDateReturn().toString());

            if (hire.getLatenessDeduction() != null) {
                pstmt.setDouble(6, hire.getLatenessDeduction());
            } else {
                pstmt.setNull(6, Types.REAL);
            }

            if (hire.getDamageDeduction() != null) {
                pstmt.setDouble(7, hire.getDamageDeduction());
            } else {
                pstmt.setNull(7, Types.REAL);
            }

            if (hire.getPaymentMade() != null) {
                pstmt.setInt(8, hire.getPaymentMade() ? 1 : 0);
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }

            pstmt.setInt(9, hire.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
