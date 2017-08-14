## 开发平台使用介绍

### 开发平台简介
为了实现快速开发，只需要少量配置即可开始业务开发，我们搭建了此开发平台
开发平台包含内容有：CAS自动接入、Redis集群缓存接入、服务中心接入、配置中心接入、日志平台接入、多数据源自动切换、消息国际化处理、tk插件组件、session共享、主键生成服务、swagger2实现接口文档生成以及针对我们项目特有的web拦截器、过滤器、异常处理等的实现。

### 引入开发平台
项目中增加如下依赖,即可引入开发平台，
```xml
<dependency>
    <groupId>com.hoau.zodiac</groupId>
    <artifactId>zodiac-springboot</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 平台功能明细及使用方法

1. 配置中心接入
框架中使用spring-cloud-consul进行配置集中管理，所以需要配置consul的服务器相关信息。由于配置中心数据的加载要在容器初始化之前，所以需要将配置文件添加到bootstrap.yml中，配置信息如下
```
  application:
    name: virgo
  cloud:
    config:
        enabled: false
    consul:
      host: ${consul:10.39.232.220}
      port: 8500
      enabled: true
      config:
        enabled: true
        format: YAML
        data-key: configuration
      discovery:
        enabled: true  #服务注册与发现是否可用
        register: true #是否注册服务,默认为 true
        catalogServicesWatchDelay: 10 #consul 连接失败重试间隔
        health-check-interval: 30s
        healthCheckPath: /${spring.application.name}/health
        fail-fast: true  #true 后，如果 consul 连不上，应用启动报错
        serviecName: ${spring.application.name}
        instance-id: ${spring.application.name}${spring.cloud.client.ipAddress}:${server.port}
        preferIpAddress: true #使用 IP 注册,true 则注册到服务中心地址为ip-address
        ip-address: ${ip:10.39.117.70}
```

2. 服务治理
- 框架中使用consul做服务治理，配置consul服务器信息参考《配置中心接入》的配置
- 向服务中心注册服务相关配置
```java
@SpringBootApplication
//允许当前服务向服务中心注册自己
@EnableDiscoveryClient
//允许使用Feign调用在服务中心注册的服务
@EnableFeignClients(basePackages = "com.hoau.*")
public class LeoApplication {
	public static void main(String[] args) {
		SpringApplication.run(LeoApplication.class, args);
	}
}
```
- 调用服务中心注册的服务
```java
@FeignClient(value = Constant.VIRGO)
public interface IdGenerator {
    @RequestMapping(value = Constant.VIRGO_URL+"/generator/id",method = RequestMethod.GET)
    long nextId();
}
```
`@FeignClient注解中制定的是目标服务在consul中注册的服务名称，即项目名称`
`@RequestMapping注解中制定的为目标服务接口方法的调用信息(接口uri、http方法)`

3. CAS接入
- 项目接入CAS相关配置
```
zodiac:
  cas:
    client:
      enable: true
      casServerUrlPrefix: https://casx.hoau.net/cas
      casServerLoginUrl: https://casx.hoau.net/cas/login
      appServerName: http://localhost:8424/leo
      redirectServerUrl: http://localhost:8424/leo
      casServerLogoutUrl: https://casx.hoau.net/cas/logout
      authenticationRedirectStrategyClass: com.hoau.leo.config.cas.DemoAuthenticationRedirectStrategy
      authenticationExclusions: /health
      validationExclusions: /health
```
- 参数说明
    - enable 接入cas统一登录
    - casServerUrlPrefix cas服务地址
    - casServerLoginUrl cas服务器登录地址
    - appServerName 当前服务器主地址
    - redirectServerUrl 登录校验失败后，重定向到cas登陆界面，登陆成功后的重定向地址，如果前后端分离，此地址需要配置成前端地址
    - casServerLogoutUrl cas服务器登出地址
    - authenticationRedirectStrategyClass 重定向策略实现类，一般情况需要实现，应用参考DemoAuthenticationRedirectStrategy重写
    - authenticationExclusions cas客户端登陆校验过滤器例外
    - validationExclusions cas客户端Ticket校验过滤器例外

4. Redis缓存
- 框架中使用Redis主从复制，哨兵模式做故障自动切换，只需要增加如下配置即可使用redis缓存服务。
```
spring:
  redis:
    enable: true
    database: 0
    password: testx
    sentinel:
      master: mymaster
      nodes: 10.39.232.213:26380,10.39.232.214:26380,10.39.232.215:26380
    pool:
      max-idle: -1
      max-wait: 3000
      max-active: -1
      min-idle: 10
```
- 框架提供`DefaultTTLRedisCache`和`DefaultStrongRedisCache`两种缓存，使用方法和hbdp相同

5. 日志平台接入(未测试)
框架使用logback+kafka，将日志写入队列，再由ELK做读取、存储、搜索分析。
- 添加依赖
```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>4.11</version>
</dependency>
<dependency>
    <groupId>com.github.danielwegener</groupId>
    <artifactId>logback-kafka-appender</artifactId>
    <version>0.1.0</version>
    <scope>runtime</scope>
</dependency>
```
- 在resource下增加logback-spring.xml文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml" />
    <springProperty scope="context" name="bootstrap.servers" source="zodiac.log.kafka.servers" />
    <!-- 测试环境.开发环境使用默认的控制台输出，如果多个环境使用相同的配置，用多个使用逗号隔开. -->
    <springProfile name="test">
        <appender name="KafkaAppender" class="com.github.danielwegener.logback.kafka.KafkaAppender">
            <encoder class="com.github.danielwegener.logback.kafka.encoding.LayoutKafkaMessageEncoder">
                <layout class="net.logstash.logback.layout.LogstashLayout" >
                    <includeContext>true</includeContext>
                    <includeCallerData>true</includeCallerData>
                    <customFields>{"system":"test"}</customFields>
                    <fieldNames class="net.logstash.logback.fieldnames.ShortenedFieldNames"/>
                </layout>
                <charset>UTF-8</charset>
            </encoder>
            <!--kafka topic 需要与配置文件里面的topic一致-->
            <topic>applog</topic>
            <keyingStrategy class="com.github.danielwegener.logback.kafka.keying.HostNameKeyingStrategy" />
            <deliveryStrategy class="com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy" />
            <producerConfig>${bootstrap.servers}</producerConfig>
        </appender>
        <!--输出到文件-->
        <property name="log.path" value="logs/logback.log" />
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${log.path}</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>logback.%d{yyyy-MM-dd}.log</fileNamePattern>
                <maxHistory>30</maxHistory>
                <totalSizeCap>1GB</totalSizeCap>
            </rollingPolicy>
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} %contextName [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        <logger name="org.springframework" level="ERROR"/>
        <logger name="org.apache.ibatis" level="INFO" />
        <logger name="org.mybatis" level="INFO" />
        <logger name="tk.mybatis" level="INFO" />
        <logger name="com.hoau" level="INFO" />
        <root>
            <level value="INFO" />
            <appender-ref ref="CONSOLE" />
            <appender-ref ref="FILE" />
        </root>
    </springProfile>

    <!-- 生产环境. -->
    <springProfile name="product">
        <appender name="KafkaAppender" class="com.github.danielwegener.logback.kafka.KafkaAppender">
            <encoder class="com.github.danielwegener.logback.kafka.encoding.LayoutKafkaMessageEncoder">
                <layout class="net.logstash.logback.layout.LogstashLayout" >
                    <includeContext>true</includeContext>
                    <includeCallerData>true</includeCallerData>
                    <customFields>{"system":"test"}</customFields>
                    <fieldNames class="net.logstash.logback.fieldnames.ShortenedFieldNames"/>
                </layout>
                <charset>UTF-8</charset>
            </encoder>
            <!--kafka topic 需要与配置文件里面的topic一致-->
            <topic>applog</topic>
            <keyingStrategy class="com.github.danielwegener.logback.kafka.keying.HostNameKeyingStrategy" />
            <deliveryStrategy class="com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy" />
            <producerConfig>${bootstrap.servers}</producerConfig>
        </appender>
        <logger name="org.springframework" level="ERROR"/>
        <logger name="org.apache.ibatis" level="INFO" />
        <logger name="org.mybatis" level="INFO" />
        <logger name="tk.mybatis" level="INFO" />
        <logger name="com.hoau" level="INFO" />
        <root>
            <level value="INFO" />
            <appender-ref ref="KafkaAppender" />
        </root>
    </springProfile>
</configuration>
```

6. 多数据源切换
- 开启多数据源切换
配置中心中增加如下配置
```
zodiac:
  dynamic:
    datasource:
      enable: true
      defaultDatasourceName: master
      datasourceList:
      - name: master
        url: jdbc:mysql://10.39.232.216:3306/leo
        username: leo_user
        password: leo
        driverClassName: com.mysql.jdbc.Driver
        maxActive: 50
        initialSize: 5
        validationQuery: select 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxOpenPreparedStatements: 20
      - name: slave
        url: jdbc:mysql://10.39.251.178:3306/sso
        username: ssouser
        password: ssopassword
        driverClassName: com.mysql.jdbc.Driver
        maxActive: 50
        initialSize: 5
        validationQuery: select 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxOpenPreparedStatements: 20
```
- 多数据源切换使用，只需要添加注解，指明使用的数据源名称即可
```
@AnotherDatasource("slave")
public User getUser(String userCode) {
}
```

7. 消息国际化
- 开启国际化配置
```
zodiac:
  message:
    enable: true
    default-country: CN
    default-lang: zh
    cookie-maxAge: 100000000
```
- 项目使用国际化
    - 在resource目录下增加默认国际化文件`messages.properties`，此文件为Locale未匹配到时的默认国际化文件，必须创建
    - 在resource目录下针对不同余元增加相应的国际化文件`messages_zh_CN.properties`、`messages_en_US.properties`
- 

8. session共享

9. 主键生成

10. swagger2接口文档生成

11. web拦截器启用

12. web过滤器启用

13. 异常处理
