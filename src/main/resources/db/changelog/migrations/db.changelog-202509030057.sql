--liquibase formatted sql
--changeset wilde:202509030057
--comment: cards table create

CREATE TABLE cards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    board_column_id BIGINT NOT NULL,
    CONSTRAINT fk_boards_columns_cards FOREIGN KEY (board_column_id) REFERENCES boards_columns(id) ON DELETE CASCADE
)ENGINE=InnoDB default charset=utf8mb4;

--rollback DROP TABLE cards;