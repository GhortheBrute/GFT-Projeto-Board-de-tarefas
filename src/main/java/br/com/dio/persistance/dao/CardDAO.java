package br.com.dio.persistance.dao;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistance.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static br.com.dio.persistance.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    private static final String INSERT_QUERY = """
                INSERT INTO cards (title, description, board_column_id)
                VALUES (?, ?, ?)
            """;

    private static final String MOVE_COLUMN_QUERY = """
                UPDATE cards SET board_column_id = ?
                WHERE id = ?
            """;

    private static final String FIND_BY_ID_QUERY = """
                SELECT c.id,
                 c.title,
                  c.description,
                   b.blocked_at,
                    b.block_reason,
                     c.board_column_id,
                      bc.name,
                       (SELECT COUNT(sub_b.id)
                        FROM blocks sub_b
                         WHERE sub_b.card_id = c.id) AS blocks_amount
                FROM cards c
                LEFT JOIN blocks b
                 ON c.id = b.card_id 
                 AND b.unblock_reason IS NULL
                INNER JOIN boards_columns bc
                 ON c.board_column_id = bc.id
                WHERE c.id = ?
            """;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        try(var stmt = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getDescription());
            stmt.setLong(3, entity.getBoardColumn().getId());
            stmt.executeUpdate();
            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        }
        return entity;
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        try(var stmt = connection.prepareStatement(MOVE_COLUMN_QUERY)) {
            stmt.setLong(1, columnId);
            stmt.setLong(2, cardId);
            stmt.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        try(var stmt = connection.prepareStatement(FIND_BY_ID_QUERY)){
            stmt.setLong(1, id);
            stmt.executeQuery();
            var rs = stmt.getResultSet();
            if (rs.next()) {
                var dto = new CardDetailsDTO(
                        rs.getLong("c.id"),
                        rs.getString("c.title"),
                        rs.getString("c.description"),
                        nonNull(rs.getString("b.block_reason")),
                        toOffsetDateTime(rs.getTimestamp("b.blocked_at")),
                        rs.getString("b.block_reason"),
                        rs.getInt("blocks_amount"),
                        rs.getLong("c.board_column_id"),
                        rs.getString("bc.name")
                );
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }
}
