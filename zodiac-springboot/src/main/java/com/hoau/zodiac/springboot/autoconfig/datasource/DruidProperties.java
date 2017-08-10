package com.hoau.zodiac.springboot.autoconfig.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 刘德云
 * @version V1.0
 * @title: DruidProperties
 * @package com.hoau.leo.config.datasource
 * @description druid 数据源配置项
 * @date 2017/8/6
 */
public class DruidProperties {

    /**
     * 数据源名称
     */
    private String name;
    /**
     * 连接数据库的url，不同数据库不一样。例如：
     * mysql : jdbc:mysql://10.20.153.104:3306/druid2
     * oracle : jdbc:oracle:thin:@10.20.149.85:1521:ocnauto
     **/
    private String url;
    /**
     * 数据库的用户名
     **/
    private String username;
    /**
     * 密码
     * 如需使用加密参考 https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
     **/
    private String password;
    /**
     * 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
     **/
    private String driverClassName;
    /**
     * 启动时初始化连接数
     **/
    private int initialSize = 10;
    /**
     * 最小连接池数量
     **/
    private int minIdle;
    /**
     * 最大连接池数量
     **/
    private int maxActive;
    /**
     * 获取连接时最大等待时间，单位毫秒
     **/
    private int maxWait;
    /**
     * 1) Destroy线程会检测连接的间隔时间
     * 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     **/
    private int timeBetweenEvictionRunsMillis;
    /**
     * 最小闲置项时间
     **/
    private int minEvictableIdleTimeMillis;
    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句
     **/
    private String validationQuery;
    /**
     * 申请连接的时候检测，如果空闲时间大于
     * timeBetweenEvictionRunsMillis，
     * 执行validationQuery检测连接是否有效
     **/
    private boolean testWhileIdle;
    /**
     * 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     **/
    private boolean testOnBorrow;
    /**
     * 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     **/
    private boolean testOnReturn;
    /**
     * 是否缓存preparedStatement，也就是PSCache
     **/
    private boolean poolPreparedStatements;
    /**
     * 属性类型是字符串，通过别名的方式配置扩展插件，
     * 常用的插件有：
     * 监控统计用的filter:stat
     * 日志用的filter:log4j
     * 防御sql注入的filter:wall
     **/
    private String filters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}
