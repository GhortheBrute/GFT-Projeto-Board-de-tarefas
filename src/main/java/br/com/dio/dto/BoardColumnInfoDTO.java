package br.com.dio.dto;

import br.com.dio.persistance.entity.KindEnum;

public record BoardColumnInfoDTO(
        Long id,
        int order,
        Enum<KindEnum> kind
) {
}
