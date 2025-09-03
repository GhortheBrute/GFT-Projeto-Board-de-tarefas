package br.com.dio.persistance.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {

    private Long id;
    private String name;
    private int order;
    private Enum<KindEnum> kind;
    private BoardEntity board = new BoardEntity();
}
