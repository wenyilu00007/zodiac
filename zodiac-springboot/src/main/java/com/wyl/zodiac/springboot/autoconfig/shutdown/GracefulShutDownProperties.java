package com.wyl.zodiac.springboot.autoconfig.shutdown;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @version V1.0
 * @title: GracefulShutDownProperties
 * @package com.wyl.scorpio.service
 * @description 延迟关闭 tomcat 配置
 * @date 2018/1/29
 */
@ConfigurationProperties(prefix = "zodiac.shutdown.graceful")
public class GracefulShutDownProperties {

    private boolean enable = false;

    private long delayTime;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }
}
