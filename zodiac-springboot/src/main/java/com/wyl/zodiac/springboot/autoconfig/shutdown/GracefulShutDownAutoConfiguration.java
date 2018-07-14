package com.wyl.zodiac.springboot.autoconfig.shutdown;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @version V1.0
 * @title: ThreadPoolAutoConfiguration
 * @package com.wyl.scorpio.service
 * @description 优雅的停止线程
 * @date 2018/1/29
 */
@Configuration
@EnableConfigurationProperties(GracefulShutDownProperties.class)
@ConditionalOnProperty(prefix = "zodiac.shutdown.graceful", name = "enable")
public class GracefulShutDownAutoConfiguration {
    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public EmbeddedServletContainerCustomizer tomcatCustomizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    ((TomcatEmbeddedServletContainerFactory) container)
                            .addConnectorCustomizers(gracefulShutdown());
                }

            }
        };
    }
}
