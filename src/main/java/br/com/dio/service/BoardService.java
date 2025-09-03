package br.com.dio.service;

import br.com.dio.persistance.dao.BoardColumnDAO;
import br.com.dio.persistance.dao.BoardDAO;
import br.com.dio.persistance.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        try{
            dao.insert(entity);
            var columns = entity.getBoardColumns()
                    .stream()
                    .peek(c -> {
                        c.setBoard(entity);
                        return c;
                    }).toList();
            for (var column: columns) {
                boardColumnDAO.insert(column);
            };
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException("Falha em criar o board.", e);
        }
        return entity;
    }

    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        try{
            if (!dao.existsById(id)) return false;

            dao.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }
}
