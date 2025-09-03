package br.com.dio.persistance.dao;

import br.com.dio.persistance.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    private static final String INSERT_QUERY = """
                INSERT INTO boards_columns (name, `order`, kind, board_id)
                VALUES (?, ?, ?, ?)
            """;

    private static final String FIND_BY_BOARD_ID_QUERY = """
                SELECT * FROM boards_columns
                WHERE board_id = ?
            """;

    public BoardColumnEntity insert(final BoardColumnEntity entity)  throws SQLException {
        try(var stmt = connection.prepareStatement(INSERT_QUERY)) {
            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getOrder());
            stmt.setString(3, entity.getKind().name());
            stmt.setLong(4, entity.getBoard().getId());
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException("Erro ao inserir a coluna.", e);
        }
        return entity;
    }

    public List<BoardColumnEntity> findByBoardId (final Long boardId) throws SQLException{
        List<BoardColumnEntity> entities = new ArrayList<>();
        try(var stmt = connection.prepareStatement(FIND_BY_BOARD_ID_QUERY)){
            stmt.setLong(1, boardId);
            stmt.executeQuery();
            var rs = stmt.getResultSet();
            while (rs.next()){
                var entity = new BoardColumnEntity();
                entity.setId(rs.getLong("id"));
                entity.setName(rs.getString("name"));
                entity.setOrder(rs.getInt("order"));
                entity.setKind(findByName(rs.getString("kind")));
                entities.add(entity);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar as colunas por board id.", e);
        }
        return entities;
    }


}
