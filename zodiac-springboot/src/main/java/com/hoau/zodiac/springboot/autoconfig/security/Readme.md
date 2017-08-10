## 访问控制拦截器

- 对用于是否有权限访问此地址进行校验拦截
- 启用拦截器
    ```
    zodiac.web.securityAccess.enable=true
    ```
- 启用此拦截器需要额外增加的内容
    - 应用必须实现`IUserResourceProvider`接口，用于获取用户可访问的所有资源的。
    - 应用必须实现`IResourceProvider`接口，用于根据当前访问uri构建完整的资源对象
    - 实现`IResource`接口的类必须重写`equals`和`hashCode`方法，用于判断用户可访问的资源中是否包含此次访问的资源