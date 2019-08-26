package com.jjx.cloudauth.config;


import com.jjx.cloudauth.mapper.AuthorityMapper;
import com.jjx.cloudauth.mapper.CredentialsMapper;
import com.jjx.cloudauth.pojo.Authority;
import com.jjx.cloudauth.pojo.AuthorityExample;
import com.jjx.cloudauth.pojo.Credentials;
import com.jjx.cloudauth.pojo.CredentialsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author jiangjx
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CredentialsMapper credentialsMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CredentialsExample credentialsExample = new CredentialsExample();
        credentialsExample.createCriteria().andNameEqualTo(username);
        List<Credentials> credentialsList = credentialsMapper.selectByExample(credentialsExample);
        Credentials credentials = credentialsList.stream().findFirst().orElse(new Credentials());
        AuthorityExample authorityExample = new AuthorityExample();
        authorityExample.createCriteria().andIdEqualTo(credentials.getId());
        List<Authority> authorities = authorityMapper.selectByExample(authorityExample);
        return new User(credentials.getName(), credentials.getPassword(), credentials.getEnabled(), true, true, true, authorities);
    }
}
