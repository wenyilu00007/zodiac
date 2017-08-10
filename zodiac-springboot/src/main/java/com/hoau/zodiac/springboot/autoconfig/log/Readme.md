## 日志

### 请求拦截日志
- 开启请求拦截日志相关配置
```
zodiac:
  log:
    # 日志拦截器
    filter:
      enable: true
      needLogRequest: true
      needLogResponse: true
      needLogHeader: true
      needLogPayload: true
      maxPayloadLength: 2097152
```

### 日志平台