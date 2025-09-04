package br.com.dio;

import br.com.dio.persistance.migration.MigrationStrategy;
import br.com.dio.ui.MainMenu;

import java.awt.*;
import java.sql.SQLException;

import static br.com.dio.persistance.config.ConnectionConfig.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        try(var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        } catch (SQLException e) {
            throw new RuntimeException("Erro na execução da migration.", e);
        }
        new MainMenu().execute();
    }
}
