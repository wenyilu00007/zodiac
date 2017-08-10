## CAS统一登录接入

- 需要增加的配置

```
# cas相关配置
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

- 应用可能需要添加的类

    - 参考`DemoAuthenticationRedirectStrategy`类的说明，如果符合条件则需要创建此类，并且配置`authenticationRedirectStrategyClass`参数为配置的类路径
    - 不使用开发平台中的这个类的原因是，此类是由cas-client反射创建的，拿不到spring容器内的配置，所以要重新创建，然后在自己的应用里配置类中的`alwaysRedirectServerUrl`参数，如果有方法可以取到，请联系我修改/(ㄒoㄒ)/~~