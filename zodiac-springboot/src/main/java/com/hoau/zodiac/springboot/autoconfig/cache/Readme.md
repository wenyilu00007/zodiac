## Redis

- 启用redis配置`spring.redis.enable=true`

- 哨兵模式集群配置
```
spring:
  redis:
    enable: true
    database: 0
    sentinel:
      master: mymaster
      nodes: 10.39.232.215:26379,10.39.232.215:26479,10.39.232.215:26579
    pool:
      max-idle: -1
      max-wait: 3000
      max-active: -1
      min-idle: 10
```