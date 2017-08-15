## Swagger

在线接口文档生成
- 开启：`zodiac.swagger.enable=true`

- 配置
```
zodiac: 
  swagger:
    enable: true
    title: ID生成服务目录
    description: 微服务之 ID 生成服务，仅用于 ID 生成
    version: 1.0
    contactName: 供应链研发中心
    paths: [/generator]
```

- 使用
```java
@ApiModel(value = "数据库配置类" )
public class DbConfiguration {
    @ApiModelProperty(value = "url", required = true, dataType = "String", example = "10.39.232.1")
    private String url;
}

@RestController
@RequestMapping(value = "/generator")
@Api(value = "/generator", description = "ID 生成微服务")
public class IdGenerateController {

    @ApiOperation(value = "测试页面是否能访问通", notes = "可以在这里详细描述接口功能")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Authentication failure", response = Response.class)
    })
    @GetMapping("/mysqlConfig")
    public String readMysqlConfig(@ApiParam DbConfiguration dbConfiguration) {
        return dbConfiguration.getUrl() + ":" + dbConfiguration.getUsername();
    }
}

```