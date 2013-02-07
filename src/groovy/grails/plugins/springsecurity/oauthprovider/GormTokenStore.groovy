package grails.plugins.springsecurity.oauthprovider


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;
import org.apache.log4j.Logger;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;

import org.springframework.security.oauth2.provider.token.*
import oauth2.*

/**
 * Implementation of token services that stores tokens in a database using gorm.
 *
 * @author Alessandro Dourado
 */
 class GormTokenStore implements TokenStore {

    def dataSource
    static Logger log = Logger.getLogger('grails.plugins.springsecurity.oauthprovider')

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        log.debug 'storeAccessToken'
        String refreshToken = null;

        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }

        log.debug 'clientId ' + authentication.clientAuthentication.clientId

        OauthClientDetails.withTransaction { status ->
            def client = OauthClientDetails.findByClientId(authentication.clientAuthentication.clientId)

            log.debug 'accessTokenValidity ' + client.accessTokenValidity
            if (client.accessTokenValidity){
                token.setExpiration(new Date(System.currentTimeMillis() + (client.accessTokenValidity * 1000L)));
            }
        } 

        def o = new OauthAccessToken(   tokenId:token.getValue(), 
                                        token: SerializationUtils.serialize(token),
                                        authentication : SerializationUtils.serialize(authentication),
                                        refreshToken : refreshToken
        )
        o.save(flush:true)

    }

    public OAuth2AccessToken readAccessToken(String tokenValue) {
        log.debug 'readAccessToken'

        log.debug 'readAccessToken'

        OAuth2AccessToken accessToken = null;

        try {

            OauthAccessToken.withTransaction { status ->
            def o = OauthAccessToken.findByTokenId(tokenValue)
            accessToken  = SerializationUtils.deserialize(o.token)
        } 

        log.debug 'accessToken.expiration '+ accessToken.expiration

        log.debug 'accessToken '+ accessToken

        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + tokenValue);
            }
        }

        return accessToken;
    }

    public void removeAccessToken(String tokenValue) {
        log.debug 'removeAccessToken'  
        OauthAccessToken.withTransaction { status ->
            def o = OauthAccessToken.findByTokenId(tokenValue)
            o.delete(flush:true)     
        } 
    }

    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        log.debug 'readAuthentication'    
        OAuth2Authentication authentication = null;

        try {

            OauthAccessToken.withTransaction { status ->
                def o = OauthAccessToken.findByTokenId(token.getValue())
                authentication  = SerializationUtils.deserialize(o.authentication)
            } 

        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + token);
            }
        }
        log.debug 'authentication ' + authentication
        return authentication;
    }

    public void storeRefreshToken(ExpiringOAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        log.debug 'storeRefreshToken'    

        def o = new OauthRefreshToken(  tokenId:refreshToken.getValue(), 
                                        token: SerializationUtils.serialize(refreshToken),
                                        authentication : SerializationUtils.serialize(authentication)
        )
        o.save(flush:true)

    }

    public ExpiringOAuth2RefreshToken readRefreshToken(String token) {
        ExpiringOAuth2RefreshToken refreshToken = null;

        try {
            refreshToken = null

            OauthRefreshToken.withTransaction { status ->
                def o = OauthRefreshToken.findByTokenId(tokenValue)
                refreshToken  = SerializationUtils.deserialize(o.token)
            } 

        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find refresh token for token " + token);
            }
        }

        return refreshToken;
    }

    public void removeRefreshToken(String token) {
        log.debug 'removeRefreshToken'    

        OauthRefreshToken.withTransaction { status ->
            def o = OauthRefreshToken.findByTokenId(token)
            o.delete(flush:true)     
        } 
    }

    public OAuth2Authentication readAuthentication(ExpiringOAuth2RefreshToken token) {
        log.debug 'readAuthentication'    
        OAuth2Authentication authentication = null;

        try {
            authentication = null

            OauthRefreshToken.withTransaction { status ->
                def o = OauthRefreshToken.findByTokenId(token.getValue())
                authentication  = SerializationUtils.deserialize(o.authentication)
            } 

        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
              LOG.info("Failed to find access token for token " + token);
            }
    }

    return authentication;
    }

    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        log.debug 'removeAccessTokenUsingRefreshToken'    

        OauthAccessToken.withTransaction { status ->
            def o = OauthAccessToken.findByRefreshToken(refreshToken)
            o.delete(flush:true)     
        } 

    }

}