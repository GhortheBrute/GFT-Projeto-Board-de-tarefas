package br.com.dio.persistance.dao;

import br.com.dio.persistance.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    private static final String INSERT_QUERY = """
                INSERT INTO boards (name)
                VALUES (?)
            """;

    private static final String DELETE_QUERY = """
                DELETE FROM boards
                WHERE id = ?
            """;

    private static final String FIND_BY_ID_QUERY = """
                SELECT id, name FROM boards
                WHERE id = ?
            """;

    private static final String EXISTS_BY_ID_QUERY = """
                SELECT 1 FROM boards
                WHERE id = ?
            """;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        try(var stmt = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getName());
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
            return entity;
        }
    }

    public void delete(final Long id) throws SQLException {
        try(var stmt = connection.prepareStatement(DELETE_QUERY)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o board por id.", e);
        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        try(var stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            stmt.setLong(1, id);
            stmt.executeQuery();
            var rs = stmt.getResultSet();
            if (rs.next()) {
                var entity = new BoardEntity();
                entity.setId(rs.getLong("id"));
                entity.setName(rs.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o board por id.", e);
        }
    }

    public boolean existsById(final Long id) throws SQLException {
        try(var stmt = connection.prepareStatement(EXISTS_BY_ID_QUERY)) {
            stmt.setLong(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o board por id.", e);
        }
    }
}
