package com.voting.associado.service;

import com.voting.associado.exception.AssociadoException;
import com.voting.associado.integration.VoteStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AssociadoRestService {

    @Value("${rest.user.info.url}")
    private String userInfoUrl;

    @Autowired
    private RestTemplate restTemplate;

    protected VoteStatusResponse getVoteStatus(String cpf) throws AssociadoException {
        final String url = String.format(this.userInfoUrl + "/users/%s", cpf);
        VoteStatusResponse voteStatus;

        try {
            final ResponseEntity<VoteStatusResponse> response = this.restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<VoteStatusResponse>() { });
            voteStatus = response.getBody();
        } catch (RestClientException e) {
            final String reason = String.format("Associado CPF %s is invalid.", cpf);
            throw new AssociadoException(reason, HttpStatus.BAD_REQUEST);
        }

        return voteStatus;
    }
}
