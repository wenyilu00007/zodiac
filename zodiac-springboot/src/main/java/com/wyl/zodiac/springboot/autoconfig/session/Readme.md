## SpringSession

- 开启SpringSession
    - 配置`spring.session.enable=true`，会导入`RedisHttpSessionConfiguration`，此配置会创建`springSessionRepositoryFilter`，此过滤器对`Request`对象进行了包装，通过`Request`对象对session的操作都会持久化到Redis中，从而实现session的共享
    - 需要配置`ConfigureRedisAction.NO_OP`， 否则会报`RedisKeyspaceNotifications`相关的错误