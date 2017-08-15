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

1. 配置中心  
以往开发中，会遇到各种配置参数，且不同环境的配置参数不同，系统中单独创建一个包处理配置文件，不仅容易出错，而且没有安全性可言。配置集中管理，将配置参数从应用中剥离出来，由配置中心实现配置的集中、一致性管理。
    - 配置中心接入
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
    - 应用中读取配置中心数据的几种方式
        - 使用spring提供的`@Value`注解
            ```
            @Value("${spring.application.name}")
            private String applicationName;
            ```
        - 使用自定义实体接收
            - 参数定义
                ```
                zodiac:
                  log:
                    # 日志拦截器
                    filter:
                      enable: true
                      needLogRequest: true
                      needLogResponse: true
                      needLogHeader: true
                      needLogPayload: true
                      maxPayloadLength: 2097152
                ```
            - 配置实体定义
                ```
                @ConfigurationProperties(prefix = "zodiac.log")
                public class LogProperties {
                    private LogFilterProperties filter;
                }
                public class LogFilterProperties {
                    private boolean enable = true;
                    private boolean needLogRequest = true;
                    private boolean needLogResponse = true;
                    private boolean needLogHeader = true;
                    private boolean needLogPayload = true;
                    private int maxPayloadLength = 2*1024*1024;
                }
                ```
            - 使用配置
                ```
                @Configuration
                @EnableConfigurationProperties(LogProperties.class)
                public class LogAutoConfiguration {
                    @Autowired
                    private LogProperties logProperties;
                }
                ```

2. 服务治理
    服务治理包含的内容比较多，如服务注册发现、服务间负载均衡、监控、降级、鉴权、上下线等等，此处只介绍跟业务开发相关连比较大的，服务的注册于发现。
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
        - `@FeignClient注解中制定的是目标服务在consul中注册的服务名称，即项目名称`
        - `@RequestMapping注解中制定的为目标服务接口方法的调用信息(接口uri、http方法)`

3. CAS接入  
如需要进行统一登录校验，则增加如下配置
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
    - 定时失效缓存`DefaultTTLRedisCache`
        - 通过继承`DefaultTTLRedisCache`，定义一个定时失效缓存， `IUser`为此缓存存储的值类型
        ```java
        @Component
        public class UserCache extends DefaultTTLRedisCache<User> {
            @Autowired
            private RedisCacheStorage redisCacheStorage;
            @Autowired
            private UserCacheProvider userCacheProvider;
            public static final String UUID = UserCache.class.getName();
            public String getUUID() {
                return UUID;
            }
            @Override
            public void afterPropertiesSet() throws Exception {
                super.afterPropertiesSet();
                setCacheStorage(redisCacheStorage);
                setCacheProvider(userCacheProvider);
                setTimeOut(10 * 60);
            }
        }
        ```
        - 通过实现`ITTLCacheProvider`接口，定义缓存数据的提供者，在的`get`方法中实现数据获取逻辑
        ```java
        @Component
        public class UserCacheProvider implements ITTLCacheProvider<User> {
            @Autowired
            private UserDao userDao;
            public User get(String userCode) {
                return userDao.getUser(userCode);
            }
        }
        ```
    - 长期有效、定时刷新缓存`DefaultStrongRedisCache`
        - 通过继承`DefaultStrongRedisCache`，定义一个定时刷新缓存，此缓存允许key为任意对象
        ```
        public class DataDictionaryCache extends DefaultStrongRedisCache<String, DataDictionaryEntity>
        ```
        - 通过实现`IBatchCacheProvider`接口，定义定时刷新缓存的数据提供者,需要实现`getLastModifyTime`和`get`两个方法。 `getLastModifyTime`方法获取数据的最新修改时间，如果最近修改时间大于上次刷新的时间，则将刷新时间之后的数据添加到缓存中
        ```
        @Component
        public class DataDictionaryCacheProvider implements IBatchCacheProvider<String, DataDictionaryEntity> {
            @Resource
            private BseDataDictionaryValueDao bseDataDictionaryValueDao;
            @Override
            public Date getLastModifyTime() {
                Long version = bseDataDictionaryValueDao.queryLastVersionNo();
                if(version == null){
                    version = 0l;
                }
                return new Date(version);
            }
    
            @Override
            public Map<String, DataDictionaryEntity> get() {
                return new HashMap<String, DataDictionaryEntity>();
            }
        }
        ```

5. 日志平台接入(未测试)  
框架使用logback+kafka，将日志写入队列，再由ELK做读取、存储、搜索分析。
    - 添加依赖
        ```
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
        ```
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
        - 项目中抛出异常、返回提示信息时，都需要使用国际化的key进行返回，框架会对返回信息进行国际化处理
            ```
            @RequestMapping(value = "/v1/user", method = RequestMethod.POST)
            public Response addUser(@RequestBody User user) {
                return returnSuccess(userService.addUser(user), MessageConstants.COMMON_ADD_SUCCESS_MESSAGE);
            }
            ```
            ```   
            public User updateUser(User user) {
                User example = new User();
                example.setUserCode(user.getUserCode());
                example.setActive(LeoConstants.ACTIVE);
                initUpdateParams(user);
                int effect = commonUserService.updateByExample(user, example);
                if (effect == 0) {
                    throw new UserException(UserException.BUSINESS_USER_UPDATE_USER_NOT_EXISTS);
                }
                return commonUserService.selectOne(example);
            }
        ```

8. session共享  
    由于SpringBoot在引用了spring-session相关的jar包后，自动配置了session共享，如果不需要进行session共享，则需要修改启动文件的默认，增加自动配置的例外
    ```java
    @SpringBootApplication
    @EnableAutoConfiguration(exclude={SessionAutoConfiguration.class})
    public class ZodiacApplication {
        public static void main(String[] args) {
            SpringApplication.run(ZodiacApplication.class, args);
        }
    }
    ```

9. 主键生成  
    - 框架中集成了主键生成的服务调用，各个业务系统中如果使用此服务，需要接入服务注册中心，接入相关配置参考《2. 服务治理》。  
    - 使用主键生成服务示例代码
        ```
        //自动注入主键生成服务
        @Autowired
        IdGenerator idGenerator;
        public long id() {
            return idGenerator.nextId();
        }
        ```

10. swagger2接口文档生成
    - swagger是什么？  
        Swagger是一款RESTFUL接口的文档在线自动生成+功能测试功能软件.
        Swagger是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。总体目标是使客户端和文件系统作为 服务器以同样的速度来更新。文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。Swagger 让部署管理和使 用功能强大的API从未如此简单。
    - 如何使用swagger？
        - 开启自动配置
            ```
            zodiac: 
              swagger:
                enable: true
                title: ID生成服务目录
                description: 微服务之 ID 生成服务，仅用于 ID 生成
                version: 1.0
                contactName: 供应链研发中心
                paths: [/generator]
            ```
        - Controller
            ```java
            @RestController
            @RequestMapping("/users")
            @Api(value = "/users", description = "用户相关操作")
            public class UserController extends BasicController {
                //不带入参的说明
                @ApiOperation(value = "获取当前登录用户", notes = "获取当前登录用户，应该在用户访问主界面后第一个调用此接口")
                @RequestMapping(value = "/v1/current", method = RequestMethod.GET)
                public Response getCurrentUser() {
                    return returnSuccess(UserContext.getCurrentUser());
                }
                //带入参的
                @ApiOperation(value = "新增用户", notes = "新增用户")
                @RequestMapping(value = "/v1/user", method = RequestMethod.POST)
                public Response addUser(@RequestBody @ApiParam User user) {
                    return returnSuccess(userService.addUser(user), MessageConstants.COMMON_ADD_SUCCESS_MESSAGE);
                }
            }
            ```
        - 实体
            ```java
            @ApiModel(value = "用户信息实体")
            public class User extends BasicEntity implements IUser {
                /**
                 * 用户登录名
                 */
                @ApiModelProperty(value = "用户编码",required = true ,dataType = "String" ,example = "279716")
                private String userCode;
            }
            ```

11. mybatis自动mapper插件
    对数据库的操作中，除了查询，其余的基本上所有的操作都是单表的增删改，自动mapper可自动实现单表的增删改查，大大提升了开发效率，开发人员只需要关心业务逻辑。
    - 开启mybatis
        ```
        zodiac:
          mybatis: 
            enable: true
        ```
    - 增加mapper自动扫描
        ```java
        @SpringBootApplication
        @MapperScan("com.hoau.**.dao")
        public class ZodiacApplication {
            public static void main(String[] args) {
                SpringApplication.run(ZodiacApplication.class, args);
            }
        }
        ```
    - 增加实体，关联数据库表
        ```
        @Table(name = "t_bse_user")
        public class User implements IUser {
        
            @Column(name = "user_name")
            private String userName;
        
            @Column(name = "emp_code")
            private String empCode;
        
            @Column(name = "emp_name")
            private String empName;
            
            setter and getter metheds ...
        }
        ```
    - 增加Dao,继承BaseDao类
        ```
        public interface UserDao extends BaseDao<User> {}
        ```
    - 增加Mapper文件(不需要写sql脚本)
        ```
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE mapper PUBLIC
                "-//mybatis.org//DTD Mapper 3.0//EN"
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
        <mapper namespace="com.hoau.zodiac.demo.dao.UserDao">
        </mapper>
        ```
    - 增加Service,继承AbstractBaseService
        ```
        @Service
        public class UserService extends AbstractBaseService<UserDao, User> implements IUserService<User> {}
        ```
    到此为止，UserService可以提供用户表的增删改查，包含根据id查询单条、根据User表的字段组合查询列表、分页、根据条件更新等功能

12. web拦截器启用
    - AccessInterceptor
        - 对用于是否有权限访问此地址进行校验拦截
        - 启用拦截器
            ```
            zodiac.web.securityAccess.enable=true
            ```
        - 启用此拦截器需要额外增加的内容
            - **应用必须实现`IUserResourceProvider`接口**，用于获取用户可访问的所有资源的。
            - **应用必须实现`IResourceProvider`接口**，用于根据当前访问uri构建完整的资源对象
            - **实现`IResource`接口的类必须重写`equals`和`hashCode`方法**，用于判断用户可访问的资源中是否包含此次访问的资源

13. web过滤器启用
    - ContextFilter   
        - 此过滤器主要作用
            - 将用户名、请求id设置到RequestContext中
            - 初始化SessionContext，可全局访问session对象
            - 将当前登录用户信息初始化到UserContext中，可全局获取用户信息
            
        - 使用此过滤器后需要额外实现的功能
            - 应用**必须提供一个实现IUserProvider接口的类**，用于根据用户名获取完整的用户信息，存储到UserContext中
            
        - 对于接入CAS统一登录的应用
            - 必须配置`zodiac.cas.client.casServerLogoutUrl`和`zodiac.cas.client.redirectServerUrl`，用于处理掉线后的跳转
        
        - 对于未接入CAS统一登录的应用
            - 平台仅支持session共享/复制的会话保持，使用cookie保持会话无效。
            - 需要设置`zodiac.web.context.withoutCasOfflineRedirectUrl`，用于掉线后的跳转，如果未设置此参数，则掉线会默认跳转到`request.getConteextPath()`对应的路径
    - LogFilter
        - 开启请求拦截日志相关配置
            ```
            zodiac:
              log:
                # 日志拦截器
                filter:
                  enable: true
                  needLogRequest: true
                  needLogResponse: true
                  needLogHeader: true
                  needLogPayload: true
                  maxPayloadLength: 2097152
            ```

14. 异常处理
    - 开发平台中的BasicController中对异常进行了统一处理，除非是刻意选择需要忽略的异常，其他所有异常都不需要进行catch！
    - 需要进行提示的业务层的异常，直接抛出BusinessException
