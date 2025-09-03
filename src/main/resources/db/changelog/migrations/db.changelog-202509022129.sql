--liquibase formatted sql
--changeset wilde:202509022129
--comment: boards table create

CREATE TABLE boards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
)ENGINE=InnoDB default charset=utf8mb4;

--rollback DROP TABLE boards;