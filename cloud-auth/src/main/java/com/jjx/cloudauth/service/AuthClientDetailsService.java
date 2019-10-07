package com.jjx.cloudauth.service;

import com.jjx.cloudauth.constant.SecurityConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @author jiangjx
 */
public class AuthClientDetailsService extends JdbcClientDetailsService {

    public AuthClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Cacheable(value = SecurityConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }

}
