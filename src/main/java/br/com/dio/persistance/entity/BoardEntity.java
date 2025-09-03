package br.com.dio.persistance.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardEntity {

    private Long id;
    private String name;
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();
}
