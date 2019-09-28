package com.jjx.cloudauth.pojo;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    private Integer id;

    private String authority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }
}