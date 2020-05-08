package com.voting.associado.service;

import com.voting.associado.integration.VoteStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AssociadoRestService {
    private static final String urlTemplate = "https://user-info.herokuapp.com/users/%s";

    @Autowired
    private RestTemplate restTemplate;

    protected VoteStatusResponse getVoteStatus(String cpf) {
        final String url = String.format(urlTemplate, cpf);
        VoteStatusResponse voteStatus;

        try {
            final ResponseEntity<VoteStatusResponse> response = this.restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<VoteStatusResponse>() { });
            voteStatus = response.getBody();
        } catch (RestClientException e) {
            final String reason = String.format("Associado CPF %s is invalid.", cpf);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }

        return voteStatus;
    }
}
