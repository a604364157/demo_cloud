package com.jjx.cloudclient.feign;

import com.jjx.cloudfile.api.common.Constant;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(Constant.SERVICE_ID)
public interface IFileApi extends com.jjx.cloudfile.api.inter.IFileApi {
}
