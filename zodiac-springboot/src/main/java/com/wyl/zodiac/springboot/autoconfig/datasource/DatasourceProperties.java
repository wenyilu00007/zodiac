package com.wyl.zodiac.springboot.autoconfig.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
* @Title: DatasourceProperties 
* @Package com.wyl.zodiac.springboot.autoconfig.datasource
* @Description: 数据源配置
* @author
* @date 2017/8/8 14:34
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.dynamic.datasource")
public class DatasourceProperties {

    /**
     * 是否启用多数据源
     */
    private boolean enable;

    /**
     * 多数据源列表
     */
    private List<DruidProperties> datasourceList;

    /**
     * 默认数据源名称
     */
    private String defaultDatasourceName;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<DruidProperties> getDatasourceList() {
        return datasourceList;
    }

    public void setDatasourceList(List<DruidProperties> datasourceList) {
        this.datasourceList = datasourceList;
    }

    public String getDefaultDatasourceName() {
        return defaultDatasourceName;
    }

    public void setDefaultDatasourceName(String defaultDatasourceName) {
        this.defaultDatasourceName = defaultDatasourceName;
    }
}
