package br.com.dio.persistance.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor (access = PRIVATE)
public final class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/board",
                System.getenv("DB_MYSQL_USER"),
                System.getenv("DB_MYSQL_PASSWORD")
        );
        connection.setAutoCommit(false);
        return connection;
    }
}
