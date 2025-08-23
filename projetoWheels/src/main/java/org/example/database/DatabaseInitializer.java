package org.example.database;

import org.example.dao.BikeDAO;
import org.example.dao.CustomerDAO;
import org.example.dao.HireDAO;
import org.example.dao.PaymentDAO;

public class DatabaseInitializer {

    public static void initialize() {
        CustomerDAO.criarTabelaSeNaoExistir();
        BikeDAO.criarTabelaSeNaoExistir();

        HireDAO.criarTabelaSeNaoExistir();
        PaymentDAO.criarTabelaSeNaoExistir();
    }
}
