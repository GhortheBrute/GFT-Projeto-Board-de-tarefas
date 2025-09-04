package br.com.dio.persistance.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.dio.persistance.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    private static final String BLOCK_QUERY = """
                INSERT INTO blocks (blocked_at, block_reason, card_id)
                VALUES (?, ?, ?)
            """;

    private static final String UNBLOCK_QUERY = """
                UPDATE blocks SET unblocked_at = ?, unblock_reason = ?
                WHERE card_id = ? AND unblock_reason IS NULL
            """;

    public void block(final String reason, final Long cardId) throws SQLException {
        try(var stmt = connection.prepareStatement(BLOCK_QUERY)) {
            stmt.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            stmt.setString(2, reason);
            stmt.setLong(3, cardId);
            stmt.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        try(var stmt = connection.prepareStatement(UNBLOCK_QUERY)) {
            stmt.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            stmt.setString(2, reason);
            stmt.setLong(3, cardId);
            stmt.executeUpdate();
        }
    }
}
