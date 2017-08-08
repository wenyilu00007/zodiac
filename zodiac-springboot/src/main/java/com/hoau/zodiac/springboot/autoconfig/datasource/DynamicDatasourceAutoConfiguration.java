package com.hoau.zodiac.springboot.autoconfig.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.hoau.zodiac.datasource.dynamic.DatasourceChangeAspect;
import com.hoau.zodiac.datasource.dynamic.DynamicDatasource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Title: DynamicDatasourceAutoConfiguration 
* @Package com.hoau.zodiac.springboot.autoconfig.datasource 
* @Description: 动态数据源自动装载
* @author 陈宇霖  
* @date 2017/8/8 15:06
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(DatasourceProperties.class)
@ConditionalOnProperty(prefix = "zodiac.dynamic.datasource", name = "enable", matchIfMissing = true)
public class DynamicDatasourceAutoConfiguration {

    Log logger = LogFactory.getLog(DynamicDatasourceAutoConfiguration.class);

    @Autowired
    private DatasourceProperties datasourceProperties;

    /**
     * 声明多数据源切换切面
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日18:45:09
     */
    @Bean
    public DatasourceChangeAspect datasourceChangeAspect() {
        DatasourceChangeAspect aspect = new DatasourceChangeAspect();
        return aspect;
    }

    /**
     * 创建动态切换的数据源
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日15:37:38
     */
    @Bean
    public DataSource dynamicDataSource() {
        DynamicDatasource datasource = new DynamicDatasource();
        List<DruidProperties> datasourceListProperties = datasourceProperties.getDatasourceList();
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        Object defaultDataSource = null;
        for (DruidProperties druidProperties : datasourceListProperties) {
            String datasourceName = druidProperties.getName();
            DataSource druidDatasource = createDruidDatasource(druidProperties);
            targetDataSources.put(datasourceName, druidDatasource);
            if (datasourceProperties.getDefaultDatasourceName().equals(druidProperties.getName())) {
                defaultDataSource = druidDatasource;
            }
        }
        datasource.setTargetDataSources(targetDataSources);
        datasource.setDefaultTargetDataSource(defaultDataSource == null ? datasourceProperties.getDefaultDatasourceName() : defaultDataSource);
        return datasource;
    }

    /**
     * 根据给定的配置创建数据源
     * @param druidProperties
     * @return
     * @author 陈宇霖
     * @date 2017年08月08日15:36:22
     */
    private DataSource createDruidDatasource(DruidProperties druidProperties) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setName(druidProperties.getName());
        datasource.setUrl(druidProperties.getUrl());
        datasource.setUsername(druidProperties.getUsername());
        datasource.setPassword(druidProperties.getPassword());
        datasource.setDriverClassName(druidProperties.getDriverClassName());
        datasource.setInitialSize(druidProperties.getInitialSize());
        datasource.setMinIdle(druidProperties.getMinIdle());
        datasource.setMaxActive(druidProperties.getMaxActive());
        datasource.setMaxWait(druidProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(druidProperties.getValidationQuery());
        datasource.setTestWhileIdle(druidProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(druidProperties.isTestOnBorrow());
        datasource.setTestOnReturn(druidProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(druidProperties.isPoolPreparedStatements());
        try {
            datasource.setFilters(druidProperties.getFilters());
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        return datasource;
    }

    /**
     * druid 数据源监控UI 界面
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", "druid");
        reg.addInitParameter("loginPassword", " leodruid");
        //白名单
        reg.addInitParameter("allow", "127.0.0.1");
        //黑名单
        //reg.addInitParameter("deny", "XX");
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        //忽略资源
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        return filterRegistrationBean;
    }
}
