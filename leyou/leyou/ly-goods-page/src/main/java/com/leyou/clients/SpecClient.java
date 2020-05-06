package com.leyou.clients;

import com.leyou.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecClient extends SpecificationApi {

}
