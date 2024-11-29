package io.aclecioscruz.service_appraiser.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosParaSolicitacao {

    private Long idCartao;
    private String cpf;
    private String endereco;
    private BigDecimal limiteLiberado;
}
