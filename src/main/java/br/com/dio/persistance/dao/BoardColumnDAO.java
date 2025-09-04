package br.com.dio.persistance.dao;

import br.com.dio.dto.BoardColumnDTO;
import br.com.dio.persistance.entity.BoardColumnEntity;
import br.com.dio.persistance.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.dio.persistance.entity.KindEnum.findByName;
import static java.util.Objects.isNull;

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

    private static final String FIND_BY_BOARD_ID_WITH_DETAILS_QUERY = """
                SELECT bc.id, bc.name, bc.kind, (SELECT COUNT(c.id) FROM cards c
                WHERE c.board_column_id = bc.id) AS card_amount
                FROM boards_columns bc
                WHERE bc.board_id = ?
                ORDER BY bc.order
            """;

    private static final String FIND_BY_ID_QUERY = """
                SELECT bc.name, bc.kind, c.id, c.title, c.description FROM boards_columns bc
                LEFT JOIN cards c ON bc.id = c.board_column_id
                WHERE bc.id = ?
            """;

    public BoardColumnEntity insert(final BoardColumnEntity entity)  throws SQLException {
        try(var stmt = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
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

    public List<BoardColumnDTO> findbyBoardIdWithDetails (final long boardId) throws SQLException{
        List<BoardColumnDTO> dtos = new ArrayList<>();
        try(var stmt = connection.prepareStatement(FIND_BY_BOARD_ID_WITH_DETAILS_QUERY)){
            stmt.setLong(1, boardId);
            stmt.executeQuery();
            var rs = stmt.getResultSet();
            while (rs.next()){
                var dto = new BoardColumnDTO(
                        rs.getLong("bc.id"),
                        rs.getString("bc.name"),
                        findByName(rs.getString("bc.kind")),
                        rs.getInt("card_amount")
                );
                dtos.add(dto);
            }
            return dtos;
        }
    }

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        try(var stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            stmt.setLong(1, id);
            stmt.executeQuery();
            var rs = stmt.getResultSet();
            if (rs.next()) {
                var entity = new BoardColumnEntity();
                entity.setName(rs.getString("bc.name"));
                entity.setKind(findByName(rs.getString("bc.kind")));
                do {
                    var card = new CardEntity();
                    if (isNull(rs.getString("c.title"))){
                        break;
                    }
                    card.setId(rs.getLong("c.id"));
                    card.setTitle(rs.getString("c.title"));
                    card.setDescription(rs.getString("c.description"));
                    entity.getCards().add(card);
                } while (rs.next());
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }
}
