/*
 * Copyright 2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.springsecurity.oauthprovider

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.apache.log4j.Logger;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.Assert;

import org.springframework.security.oauth2.provider.token.*
import org.springframework.security.oauth2.provider.*
import oauth2.*

/**
 * Basic, GORM implementation of the client details service.
 *
 * @author Alessandro Dourado
 */
public class GormClientDetailsService implements ClientDetailsService {
  
    static Logger log = Logger.getLogger('grails.plugins.springsecurity.oauthprovider')

    public ClientDetails loadClientByClientId(String clientId) throws OAuth2Exception {

        log.debug 'loadClientByClientId... ' + clientId
        ClientDetails details;
        try {

            OauthClientDetails.withTransaction { status ->
            def o = OauthClientDetails.findByClientId(clientId)

                details = new BaseClientDetails(o.resourceIds,
                                                o.scope, 
                                                o.authorizedGrantTypes, 
                                                o.authorities
                )
                details.clientSecret = o.clientSecret
                details.webServerRedirectUri = o.webServerRedirectUri

            } 

            log.debug 'loadClientByClientId... details ' + details

        } catch (EmptyResultDataAccessException e) {
            throw new InvalidClientException("Client not found: " + clientId);
        }

        return details;
    }
 
}