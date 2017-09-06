mybatis默认的日志实现中没有logback，所以此处增加logback日志实现方式，使用时只需要在application.yml中配置实现类即可
```
mybatis:
  configuration:
    log-impl: com.hoau.zodiac.log.LogbackForMybatis
```