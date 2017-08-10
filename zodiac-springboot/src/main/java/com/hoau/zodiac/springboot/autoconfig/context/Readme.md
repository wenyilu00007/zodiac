## Context过滤器

- 此过滤器主要作用
    - 将用户名、请求id设置到RequestContext中
    - 初始化SessionContext，可全局访问session对象
    - 将当前登录用户信息初始化到UserContext中，可全局获取用户信息
    
- 使用此过滤器后需要额外实现的功能
    - 应用必须提供一个实现IUserProvider接口的类，用于根据用户名获取完整的用户信息，存储到UserContext中
    
- 对于接入CAS统一登录的应用
    - 必须配置`zodiac.cas.client.casServerLogoutUrl`和`zodiac.cas.client.redirectServerUrl`，用于处理掉线后的跳转

- 对于未接入CAS统一登录的应用
    - 平台仅支持session共享/复制的会话保持，使用cookie保持会话无效。
    - 需要设置`zodiac.web.context.withoutCasOfflineRedirectUrl`，用于掉线后的跳转，如果未设置此参数，则掉线会默认跳转到`request.getConteextPath()`对应的路径