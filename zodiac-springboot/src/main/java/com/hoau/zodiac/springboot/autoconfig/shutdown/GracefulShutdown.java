package com.hoau.zodiac.springboot.autoconfig.shutdown;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘德云
 * @version V1.0
 * @title: ContextClosedHandler
 * @package com.hoau.scorpio.service
 * @description 线程优雅退出监听
 * @date 2018/1/29
 */
public class GracefulShutdown implements TomcatConnectorCustomizer,ApplicationListener<ContextClosedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private volatile Connector connector;

    @Autowired
    GracefulShutDownProperties gracefulShutDownProperties;


    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.connector.pause();
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                logger.info("shutdown graceful:"+gracefulShutDownProperties.isEnable()+",will shutdown in"+gracefulShutDownProperties.getDelayTime());
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(gracefulShutDownProperties.getDelayTime(), TimeUnit.SECONDS)) {
                    logger.warn("Tomcat thread pool did not shut down gracefully within "
                            +gracefulShutDownProperties.getDelayTime() + "seconds. Proceeding with forceful shutdown");
                }
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}