### 主键生成器组件
通过服务注册中心调用主键生成服务 virgo，返回一个 long 类型的主键

### 须知

 - 该系统所在服务中心必须已经注册 virgo 服务
 - 该系统需要至少引入 zodiac-core 包
 - springboot 启动类需要增加 注解```@EnableFeignClients(basePackages = "com.wyl.*")``` ```@EnableDiscoveryClient```
 ```
 @SpringBootApplication
 @ComponentScan(basePackages = "com.wyl.*")
 @EnableDiscoveryClient
 @EnableFeignClients(basePackages = "com.wyl.*")
 public class LeoApplication {
 
 	public static void main(String[] args) {
 		SpringApplication.run(LeoApplication.class, args);
 	}
 }
 ```
 - 需要生成主键时直接注入，调用
 ```
    @Autowired
    IdGenerator idGenerator;

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public long id() {
        return idGenerator.nextId();
    }
 ```