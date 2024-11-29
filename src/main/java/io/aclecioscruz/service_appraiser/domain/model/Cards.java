package io.aclecioscruz.service_appraiser.domain.model;

import io.aclecioscruz.service_appraiser.enums.EFlag;

import java.math.BigDecimal;

public record Cards (
        String nome, EFlag flag, BigDecimal limite
){
}
