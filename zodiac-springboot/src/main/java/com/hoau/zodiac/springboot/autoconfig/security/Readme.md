## 安全校验

### 访问控制拦截器

- 对用于是否有权限访问此地址进行校验拦截
- 启用拦截器
    ```
    zodiac.security.accessControl.enable=true
    ```
- 启用此拦截器需要额外增加的内容
    - 应用必须实现`IUserResourceProvider`接口，用于获取用户可访问的所有资源的。
    - 应用必须实现`IResourceProvider`接口，用于根据当前访问uri构建完整的资源对象
    - 实现`IResource`接口的类必须重写`equals`和`hashCode`方法，用于判断用户可访问的资源中是否包含此次访问的资源
    
### ip白名单过滤器

- 对访问的客户端进行校验，是否在允许访问白名单内
- 启用过滤器配置
    ```
    zodiac:
      security:
        ip:
          whitelist:
            enable: true
            whiteList: [127.0.0.1, 10.39.232.220, 10.39.232.218, 10.29.232.219, 169.254.221.43]
    ```
    
### 参数签名校验过滤器

- 主要对后台接口调用的数据来源、是否进行篡改进行校验
- 启用过滤器配置
    ```
    zodiac:
      verify:
        enable: false
        requestTimeDelaySeconds: 180
        excludeUrlPatterns: [/health, /users/*/actions/login,/users/*/actions/logout,/swagger*]
    ```
- 提供接口的应用，需要向调用方颁发apiKey及其对应的securityKey
- 调用方调用接口时遵循以下方法
    - 头信息中必须包含调用接口时间、apiKey、token，且全部必填，token为参数+securityKey加密结果，
    - GET请求token生成逻辑：
        1. 将查询参数存放到map中
        2. 调用`MD5Utils.sign(MapUtils.createLinkString(MapUtils.removeEmpty(requestParams)), SECURITY_KEY, "UTF-8")`即可生成token
    - 非GET请求token生成逻辑
        1. 将请求体数据使用fastjson转换成JSON字符串
        2. 调用`MD5Utils.sign(requestBody, SECURITY_KEY, "UTF-8")`即可生成token