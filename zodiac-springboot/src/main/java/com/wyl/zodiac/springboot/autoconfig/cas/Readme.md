## CAS统一登录接入

- 需要增加的配置

```
# cas相关配置
zodiac:
  cas:
    client:
      enable: true
      casServerUrlPrefix: https://casx.wyl.net/cas
      casServerLoginUrl: https://casx.wyl.net/cas/login
      appServerName: http://localhost:8424/leo/redirect
      casServerLogoutUrl: https://casx.wyl.net/cas/logout
      authenticationRedirectStrategyClass: com.wyl.zodiac.springboot.autoconfig.cas.ZodiacAuthenticationRedirectStrategy
      authenticationExclusions: /autoconfig
      validationExclusions: /autoconfig
      redirectAfterValidation: false
      redirectServerUrl: http://localhost:8424/leo/redirect
      fontMainPageUrl: http://localhost:8089
```

- 配置说明
    - enable: 开启cas登陆校验过滤器
    - casServerUrlPrefix: CAS服务地址
    - casServerLoginUrl: CAS服务器登陆地址
    - appServerName: 当前接入cas的应用服务器地址，注意，前后端分离的时候，这个地址需要配置成后端地址
    - casServerLogoutUrl: CAS服务器登出地址
    - authenticationRedirectStrategyClass: 用户未登录时，重定向到cas登陆界面时的重定向策略，前后端分离的时候，使用Ajax发送异步请求时，不能使用浏览器的302重定向，在此类中进行处理
    - authenticationExclusions: 不进行登陆校验的地址
    - validationExclusions: 不进行ticket验证的地址
    - redirectAfterValidation: ticket校验通过后是否进行重定向，前后端分离的时候，使用Ajax，不能进行重定向
    - redirectServerUrl: 前后端分离后，需要从后端进行重定向到前台的首页，此地址为后端进行重定向的接口地址。cas认证失败后，重定向到登陆页，登陆成功后需要重定向到此地址后，再重定向到前台主界面
    - fontMainPageUrl: 前台首页，`redirectServerUrl`接口中重定向的目标地址