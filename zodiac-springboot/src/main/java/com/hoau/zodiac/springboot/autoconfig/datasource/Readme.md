## 多数据源

- 开启多数据源配置
```
zodiac: 
  dynamic: 
    datasource: 
      enable: true
```

- 设置默认数据源
```
zodiac: 
  dynamic: 
    datasource: 
      defaultDatasourceName: master
```

- 多个数据源配置
```
zodiac:
  dynamic:
    datasource:
      datasourceList:
      - name: master
        url: jdbc:mysql://10.39.232.216:3306/leo
        username: leo_user
        password: leo
        driverClassName: com.mysql.jdbc.Driver
        maxActive: 200
      - name: slave
        url: jdbc:mysql://10.39.251.178:3306/sso
        username: ssouser
        password: ssopassword
        driverClassName: com.mysql.jdbc.Driver
        maxActive: 200
```

- 多数据切换示例(在service层进行数据源的切换)
    - 使用@AnotherDatasource注解，并指定切换的数据源实现
```java
@Component
public class UserService {
    @AnotherDatasource("slave")
    public User getUserFromAnotherDB(String userName) {
        return commonUserService.getUser(userName);
    }
}
```

## Mybatis启用
- 设置`zodiac.mybatis.enable=true`开启mybatis使用。

***注意***
由于mybatis中使用的Database是注入的，容器中只能有一个Database对象，所以，这里没有对单个数据源做配置的处理。如果只需要使用单个数据源，则配置`datasourceList`时只配置单个数据源即可