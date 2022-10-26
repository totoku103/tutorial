package me.totoku103.tutorial.authorization.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.totoku103.tutorial.authorization.entity.ClientDetailEntity;
import me.totoku103.tutorial.authorization.repository.ClientDetailRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomClientDetailService implements ClientDetailsService, ClientRegistrationService {
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final ClientDetailRepository clientDetailRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        final ClientDetailEntity entity =
                clientDetailRepository
                        .findById(clientId)
                        .orElseThrow(() -> new UnauthorizedClientException(clientId));

        return new BaseClientDetails(entity.getClientId(), entity.getResourceIds(), entity.getScope(), entity.getAuthorizedGrantTypes(), entity.getAuthorities());
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        final ClientDetailEntity entity = new ClientDetailEntity();
        entity.setClientId(clientDetails.getClientId());
        entity.setResourceIds(clientDetails.getResourceIds().stream().collect(Collectors.joining(",")));
        setEtcInfo(clientDetails, entity);
        clientDetailRepository.save(entity);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        final ClientDetailEntity entity =
                clientDetailRepository
                        .findById(clientDetails.getClientId())
                        .orElseThrow(() -> new UnauthorizedClientException(clientDetails.getClientId()));

        entity.setResourceIds(String.join(",", clientDetails.getResourceIds()));
        setEtcInfo(clientDetails, entity);
    }

    private void setEtcInfo(ClientDetails clientDetails, ClientDetailEntity entity) {
        entity.setClientSecret(this.passwordEncoder.encode(clientDetails.getClientSecret()));
        entity.setScope(clientDetails.getScope().stream().collect(Collectors.joining(",")));
        entity.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes().stream().collect(Collectors.joining(",")));
        entity.setWebServerRedirectUri(clientDetails.getRegisteredRedirectUri().stream().collect(Collectors.joining(",")));
        entity.setAuthorities(clientDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        entity.setAccessTokenValidity(clientDetails.getAccessTokenValiditySeconds());
        entity.setRefreshTokenValidity(clientDetails.getRefreshTokenValiditySeconds());
        try {
            entity.setAdditionalInformation(objectMapper.writeValueAsString(clientDetails.getAdditionalInformation()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        final ClientDetailEntity entity =
                clientDetailRepository
                        .findById(clientId)
                        .orElseThrow(() -> new UnauthorizedClientException(clientId));
        entity.setClientSecret(this.passwordEncoder.encode(secret));
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        clientDetailRepository.deleteByClientId(clientId);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return clientDetailRepository
                .findAll()
                .stream()
                .map(entity -> new BaseClientDetails(entity.getClientId(), entity.getResourceIds(), entity.getScope(), entity.getAuthorizedGrantTypes(), entity.getAuthorities()))
                .collect(Collectors.toList());
    }
}
