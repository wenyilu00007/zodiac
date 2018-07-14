package com.wyl.zodiac.core.generation;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author
 * @version V1.0
 * @title: service
 * @package com.wyl.zodiac.core
 * @description 主键生成服务
 * @date 2017/8/9
 */
@FeignClient(value = "virgo-id")
public interface IdGenerator {

    @RequestMapping(value = "/generator/id",method = RequestMethod.GET)
    String nextId();
}
