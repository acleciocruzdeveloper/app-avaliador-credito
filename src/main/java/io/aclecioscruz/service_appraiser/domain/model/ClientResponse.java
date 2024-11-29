package io.aclecioscruz.service_appraiser.domain.model;

import java.util.List;

public record ClientResponse(
        DadaClient client,
        List<Cards> cardsList
) {
}
