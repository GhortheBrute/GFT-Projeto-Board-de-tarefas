package br.com.dio.dto;

import br.com.dio.persistance.entity.KindEnum;

public record BoardColumnDTO(
        Long id,
        String name,
        KindEnum kind,
        int cardsAmount) {
}
