package it.thimoty.augmentedcoding.m08;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Fixture SAST: questa query e' volutamente vulnerabile. */
public final class CheckoutRepository {
    private final Connection connection;

    public CheckoutRepository(Connection connection) {
        this.connection = connection;
    }

    public String findOrderByCustomer(String customerId) throws SQLException {
        String sql = "SELECT order_id FROM orders WHERE customer_id = '" + customerId + "'";
        try (Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(sql)) {
            return rows.next() ? rows.getString("order_id") : null;
        }
    }
}
