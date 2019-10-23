package com.beyondsoft.identityserver.service;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by WenChao on 2019/8/16.
 */
@Component
public class ClientServiceImpl extends JdbcClientDetailsService {

    public ClientServiceImpl(DataSource dataSource) {
        super(dataSource);
    }
}
