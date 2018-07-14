package com.wyl.zodiac.springboot.autoconfig.id;

import com.wyl.zodiac.core.util.id.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @version V1.0
 * @title: IdGenerateAutoConfiguration
 * @package com.wyl.virgo.virgo.genneration
 * @description
 * @date 2017/8/8
 */
@Configuration
@EnableConfigurationProperties(IdGenerateProperties.class)
public class IdGenerateAutoConfiguration {

    @Autowired
    IdGenerateProperties idGenerateProperties;

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeIdWorker init(){
        return new SnowflakeIdWorker(idGenerateProperties.getWorkId(),idGenerateProperties.getDatacenterId());
    }

}
