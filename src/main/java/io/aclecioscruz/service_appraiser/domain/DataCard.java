package io.aclecioscruz.service_appraiser.domain;

import java.math.BigDecimal;

public record DataCard(
        String nome, String flag, BigDecimal limite
) {
}
