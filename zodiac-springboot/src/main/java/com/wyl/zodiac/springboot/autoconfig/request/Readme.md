## RequestHolder

- 此配置用于增加`RequestContextListener`监听，可在请求时将`Request`对象静态化到`RequestContextHolder`中，在全局可使用`((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()`获取到本次请求的`Request`对象
- 开启： `zodiac.request.holder.enable=true`