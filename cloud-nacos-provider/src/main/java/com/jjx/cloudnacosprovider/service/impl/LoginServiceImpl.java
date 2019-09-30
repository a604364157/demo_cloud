package com.jjx.cloudnacosprovider.service.impl;

import com.jjx.cloudnacosprovider.entity.Login;
import com.jjx.cloudnacosprovider.mapper.ILoginMapper;
import com.jjx.cloudnacosprovider.service.ILoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-30
 */
@Primary
@Service
public class LoginServiceImpl extends ServiceImpl<ILoginMapper, Login> implements ILoginService {

}
