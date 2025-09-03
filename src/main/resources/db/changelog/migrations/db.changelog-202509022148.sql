--liquibase formatted sql
--changeset wilde:202509022148
--comment: boards_columns table create

CREATE TABLE boards_columns (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    `order` INT NOT NULL,
    kind ENUM('Inicial', 'Pendente', 'Cancelamento', 'Final') NOT NULL DEFAULT 'Inicial',
    board_id BIGINT NOT NULL,
    CONSTRAINT fk_boards_boards_columns FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY unique_board_id_order (board_id, `order`)
)ENGINE=InnoDB default charset=utf8mb4;

--rollback DROP TABLE boards;