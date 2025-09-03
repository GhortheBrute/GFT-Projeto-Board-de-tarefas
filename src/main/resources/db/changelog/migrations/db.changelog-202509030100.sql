--liquibase formatted sql
--changeset wilde:202509030100
--comment: blocks table create

CREATE TABLE blocks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    blocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    blocked_reason VARCHAR(255) NOT NULL,
    unblocked_at TIMESTAMP NULL,
    unblock_reason VARCHAR(255) NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT fk_cards_blocks FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
)ENGINE=InnoDB default charset=utf8mb4;

--rollback DROP TABLE blocks;