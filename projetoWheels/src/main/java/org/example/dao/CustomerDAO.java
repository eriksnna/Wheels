package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public CustomerDAO() {
        criarTabelaSeNaoExistir();
    }

    public static void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_name TEXT NOT NULL,
                customer_address TEXT NOT NULL,
                customer_telephone_number TEXT NOT NULL,
                is_student BOOLEAN NOT NULL,
                is_senior BOOLEAN NOT NULL
            );
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void salvar(Customer c) {
        String sql = """
            INSERT INTO customers (
                customer_name,
                customer_address,
                customer_telephone_number,
                is_student,
                is_senior
            ) VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getCustomerName());
            stmt.setString(2, c.getCustomerAddress());
            stmt.setString(3, c.getCustomerTelephoneNumber());
            stmt.setBoolean(4, c.getIsStudent());
            stmt.setBoolean(5, c.getIsSenior());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Customer> listarTodos() {
        List<Customer> lista = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setCustomerAddress(rs.getString("customer_address"));
                c.setCustomerTelephoneNumber(rs.getString("customer_telephone_number"));
                c.setIsStudent(rs.getBoolean("is_student"));
                c.setIsSenior(rs.getBoolean("is_senior"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void atualizar(Customer c) {
        String sql = """
            UPDATE customers
            SET customer_name = ?,
                customer_address = ?,
                customer_telephone_number = ?,
                is_student = ?,
                is_senior = ?
            WHERE customer_id = ?
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getCustomerName());
            stmt.setString(2, c.getCustomerAddress());
            stmt.setString(3, c.getCustomerTelephoneNumber());
            stmt.setBoolean(4, c.getIsStudent());
            stmt.setBoolean(5, c.getIsSenior());
            stmt.setInt(6, c.getCustomerId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Customer buscarPorId(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setCustomerId(rs.getInt("customer_id"));
                    c.setCustomerName(rs.getString("customer_name"));
                    c.setCustomerAddress(rs.getString("customer_address"));
                    c.setCustomerTelephoneNumber(rs.getString("customer_telephone_number"));
                    c.setIsStudent(rs.getBoolean("is_student"));
                    c.setIsSenior(rs.getBoolean("is_senior"));
                    return c;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
