package br.com.dio.persistance.migration;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import static br.com.dio.persistance.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class MigrationStrategy {

    private final Connection connection;

    private static final String CHANGELOG_PATH = "/db/changelog/db.changelog-master.yml";
    private static final String LOG_FILE = "liquibase.log";

    public void executeMigration() {
           final PrintStream originalOut = System.out;
           final PrintStream originalErr = System.err;
           try{
               try(var fos = new FileOutputStream(LOG_FILE)) {
                   System.setOut(new PrintStream(fos));
                   System.setErr(new PrintStream(fos));
                   try (
                           var connection = getConnection();
                           var jdbcConnection = new JdbcConnection(connection)
                   ) {
                       var liquibase = new Liquibase(
                               CHANGELOG_PATH,
                               new ClassLoaderResourceAccessor(),
                               jdbcConnection);
                       liquibase.update("");
                   } catch (SQLException | LiquibaseException e) {
                       System.setErr(originalErr);
                       throw new RuntimeException("Erro ao executar migration", e);
                   }
               }
               } catch (IOException e) {
                   throw new RuntimeException("Error writing Liquibase log file", e);
               } finally {
                   System.setOut(originalOut);
                   System.setErr(originalErr);
           }

    }
}
