package me.totoku103.tutorial.authorization.service;

import lombok.extern.slf4j.Slf4j;
import me.totoku103.tutorial.authorization.entity.AccessTokenEntity;
import me.totoku103.tutorial.authorization.entity.RefreshTokenEntity;
import me.totoku103.tutorial.authorization.repository.AccessTokenRepository;
import me.totoku103.tutorial.authorization.repository.RefreshTokenRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomTokenStoreService implements TokenStore {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    public CustomTokenStoreService(AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        final String tokenId = extractTokenKey(token);
        final AccessTokenEntity accessTokenEntity = this.accessTokenRepository.findByTokenId(tokenId);

        if (accessTokenEntity == null) {
            log.info("not found access token. {}", tokenId);
            return null;
        }

        try {
            return deserializeAuthentication(accessTokenEntity.getAuthentication());
        } catch (IllegalArgumentException e) {
            log.error("Failed to deserialize authentication.", e);
            removeAccessToken(token);
            return null;
        }
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }

        if (readAccessToken(token.getValue()) != null) {
            removeAccessToken(token.getValue());
        }

        final AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
        accessTokenEntity.setTokenId(extractTokenKey(token.getValue()));
        accessTokenEntity.setToken(serializeAccessToken(token));
        accessTokenEntity.setUserName(authentication.isClientOnly() ? null : authentication.getName());
        accessTokenEntity.setClientId(authentication.getOAuth2Request().getClientId());
        accessTokenEntity.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        accessTokenEntity.setAuthentication(serializeAuthentication(authentication));
        accessTokenEntity.setRefreshToken(extractTokenKey(refreshToken));

        this.accessTokenRepository.save(accessTokenEntity);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        final String tokenId = extractTokenKey(tokenValue);
        final AccessTokenEntity accessTokenEntity = this.accessTokenRepository.findByTokenId(tokenId);

        if (accessTokenEntity == null) {
            log.info("not found access token. {}", tokenId);
            return null;
        }
        try {
            return deserializeAccessToken(accessTokenEntity.getToken());
        } catch (IllegalArgumentException e) {
            log.error("Failed to deserialize access token", e);
            removeAccessToken(tokenValue);
            return null;
        }
    }

    public void removeAccessToken(String tokenValue) {
        final String tokenId = extractTokenKey(tokenValue);
        final int result = this.accessTokenRepository.deleteByTokenId(tokenId);
        if (result == 0)
            log.debug("failed delete access token. {}", tokenId);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        final RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setTokenId(extractTokenKey(refreshToken.getValue()));
        refreshTokenEntity.setToken(serializeRefreshToken(refreshToken));
        refreshTokenEntity.setAuthentication(serializeAuthentication(authentication));

        final RefreshTokenEntity result = this.refreshTokenRepository.save(refreshTokenEntity);
        if (result == null)
            log.debug("failed save");
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        final String tokenId = extractTokenKey(tokenValue);
        final RefreshTokenEntity refreshTokenEntity = this.refreshTokenRepository.findByTokenId(tokenId);

        if (refreshTokenEntity == null) {
            log.info("not found refresh token. {}", tokenId);
            return null;
        }

        try {
            return deserializeRefreshToken(refreshTokenEntity.getToken());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deserialize refresh token", e);
            removeRefreshToken(tokenValue);
            return null;
        }
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String tokenValue) {
        final String tokenId = extractTokenKey(tokenValue);
        final RefreshTokenEntity refreshTokenEntity = this.refreshTokenRepository.findByTokenId(tokenId);

        if (refreshTokenEntity == null) {
            log.info("not found refresh token. {}", tokenId);
            return null;
        }

        try {
            return deserializeAuthentication(refreshTokenEntity.getAuthentication());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deserialize refresh token", e);
            removeRefreshToken(tokenValue);
            return null;
        }
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        final String tokenId = extractTokenKey(tokenValue);
        final int result = this.refreshTokenRepository.deleteByTokenId(tokenId);

        if (result == 0)
            log.debug("failed delete");
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(String tokenValue) {
        final String refreshTokenId = extractTokenKey(tokenValue);
        final int result = this.accessTokenRepository.deleteByRefreshToken(refreshTokenId);

        if (result == 0)
            log.debug("failed delete");
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        final String authenticationId = authenticationKeyGenerator.extractKey(authentication);
        final AccessTokenEntity accessTokenEntity = this.accessTokenRepository.findByAuthenticationId(authenticationId);

        if (accessTokenEntity == null) {
            log.error("Failed to find access token for authentication. {}", authentication);
            return null;
        }

        try {
            final OAuth2AccessToken accessToken = deserializeAccessToken(accessTokenEntity.getToken());
            if (accessToken == null) return null;

            final OAuth2Authentication oAuth2Authentication = readAuthentication(accessToken.getValue());
            if (oAuth2Authentication == null || !authenticationId.equals(authenticationKeyGenerator.extractKey(oAuth2Authentication))) {
                removeAccessToken(accessToken.getValue());
                storeAccessToken(accessToken, authentication);
            }
            return accessToken;
        } catch (IllegalArgumentException e) {
            log.info("Could not extract access token for authentication " + authentication, e);
            return null;
        }
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        final List<AccessTokenEntity> accessTokenEntities = this.accessTokenRepository.findByUserNameAndClientId(userName, clientId);
        if (accessTokenEntities == null) {
            log.info("Failed to find access token for clientId " + clientId + " and userName " + userName);
            return new ArrayList<>();
        }
        final List<OAuth2AccessToken> oAuth2AccessTokens = deserializeAccessToken(accessTokenEntities);
        return removeNulls(oAuth2AccessTokens);
    }

    private List<OAuth2AccessToken> removeNulls(List<OAuth2AccessToken> accessTokens) {
        List<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        for (OAuth2AccessToken token : accessTokens) {
            if (token != null) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        final List<AccessTokenEntity> accessTokenEntities = this.accessTokenRepository.findByClientId(clientId);
        if (CollectionUtils.isEmpty(accessTokenEntities)) {
            log.info("Failed to find access token for clientId " + clientId);
            return new ArrayList<>();
        }

        final List<OAuth2AccessToken> oAuth2AccessTokens = deserializeAccessToken(accessTokenEntities);
        return removeNulls(oAuth2AccessTokens);
    }

    private List<OAuth2AccessToken> deserializeAccessToken(List<AccessTokenEntity> accessTokenEntities) {
        return accessTokenEntities
                .stream()
                .map(d -> {
                    try {
                        return deserializeAccessToken(d.getToken());
                    } catch (IllegalArgumentException e) {
                        this.accessTokenRepository.deleteByTokenId(d.getTokenId());
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }
}
