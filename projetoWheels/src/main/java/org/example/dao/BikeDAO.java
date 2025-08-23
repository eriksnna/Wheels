package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.models.Bike;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BikeDAO {

    public BikeDAO() {
        criarTabelaSeNaoExistir();
    }

    public static void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS bikes (
                bike_id INTEGER PRIMARY KEY AUTOINCREMENT,
                bike_type TEXT NOT NULL,
                bike_brand TEXT NOT NULL,
                bike_model TEXT NOT NULL,
                is_available BOOLEAN NOT NULL,
                rental_price REAL NOT NULL,
                deposit_price REAL NOT NULL
            );
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void salvar(Bike bike) {
        String sql = "INSERT INTO bikes (bike_type, bike_brand, bike_model, is_available, rental_price, deposit_price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bike.getBikeType());
            stmt.setString(2, bike.getBikeBrand());
            stmt.setString(3, bike.getBikeModel());
            stmt.setBoolean(4, bike.isAvailable());
            stmt.setDouble(5, bike.getRentalPrice());
            stmt.setDouble(6, bike.getDepositPrice());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Bike> listarTodos() {
        List<Bike> lista = new ArrayList<>();
        String sql = "SELECT * FROM bikes";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bike bike = new Bike();
                bike.setBikeId(rs.getInt("bike_id"));
                bike.setBikeType(rs.getString("bike_type"));
                bike.setBikeBrand(rs.getString("bike_brand"));
                bike.setBikeModel(rs.getString("bike_model"));
                bike.setIsAvailable(rs.getBoolean("is_available"));
                bike.setRentalPrice(rs.getDouble("rental_price"));
                bike.setDepositPrice(rs.getDouble("deposit_price"));
                lista.add(bike);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void atualizar(Bike bike) {
        String sql = """
            UPDATE bikes
            SET bike_type = ?,
                bike_brand = ?,
                bike_model = ?,
                is_available = ?,
                rental_price = ?,
                deposit_price = ?
            WHERE bike_id = ?
            """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bike.getBikeType());
            stmt.setString(2, bike.getBikeBrand());
            stmt.setString(3, bike.getBikeModel());
            stmt.setBoolean(4, bike.isAvailable());
            stmt.setDouble(5, bike.getRentalPrice());
            stmt.setDouble(6, bike.getDepositPrice());
            stmt.setInt(7, bike.getBikeId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Bike buscarPorId(int id) {
        String sql = "SELECT * FROM bikes WHERE bike_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bike bike = new Bike();
                    bike.setBikeId(rs.getInt("bike_id"));
                    bike.setBikeType(rs.getString("bike_type"));
                    bike.setBikeBrand(rs.getString("bike_brand"));
                    bike.setBikeModel(rs.getString("bike_model"));
                    bike.setIsAvailable(rs.getBoolean("is_available"));
                    bike.setRentalPrice(rs.getDouble("rental_price"));
                    bike.setDepositPrice(rs.getDouble("deposit_price"));
                    return bike;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
