package com.jjx.cloudauth.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjx.cloudauth.constant.SecurityConstants;
import com.jjx.cloudauth.entity.AuthUser;
import com.jjx.cloudauth.entity.Credentials;
import com.jjx.cloudauth.mapper.ICredentialsMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 用户详细信息
 *
 * @author jiangjx
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ICredentialsMapper credentialsMapper;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return user
	 */
	@Override
	@SneakyThrows
	@Cacheable(value = SecurityConstants.USER_DETAILS, key = "#username", unless = "#result == null")
	public UserDetails loadUserByUsername(String username) {
		Wrapper<Credentials> query = Wrappers.<Credentials>lambdaQuery().eq(Credentials::getName, username);
		Credentials credentials = credentialsMapper.selectOne(query);
		return new AuthUser(0, 0, credentials.getName(), credentials.getPassword(), credentials.getEnabled(), true, true,true, null);
	}
}
