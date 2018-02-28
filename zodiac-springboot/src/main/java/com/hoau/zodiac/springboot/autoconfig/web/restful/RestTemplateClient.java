package com.hoau.zodiac.springboot.autoconfig.web.restful;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈宇霖
 * @version V1.0
 * @Title: RestTemplateClient
 * @Package com.hoau.hbdp.webservice.components.rest.client
 * @Description: Rest接口调用客户端
 * @date 2017/07/04 15:03:14
 */
public class RestTemplateClient implements InitializingBean {
    /**
     * Rest 调用
     */
    private RestTemplate restTemplate;

    /**
     * 使用fastjson做消息转换器的resetTemplate
     */
    private RestTemplate fastjsonConverterRestTemplate;

    /**
     * 连接工厂相关设置
     */
    private RestfulTemplateProperties restfulTemplateProperties;

    /**
     * 拦截器
     */
    private List<ClientHttpRequestInterceptor> interceptors;

    /**
     * 使用fastjson做消息转换器的RestTemplate的拦截器
     */
    private List<ClientHttpRequestInterceptor> fastjsonInterceptors;

    /**
     * fastjson转换器
     */
    private FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4;

    public void afterPropertiesSet() throws Exception {
        restTemplate = new RestTemplate();
        // httpClient连接池配置
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager();
        pollingConnectionManager.setMaxTotal(restfulTemplateProperties.getMaxTotal());
        pollingConnectionManager.setDefaultMaxPerRoute(restfulTemplateProperties.getDefaultMaxPerRoute());
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        // 连接超时时间
        requestBuilder = requestBuilder.setConnectTimeout(restfulTemplateProperties.getConnectTimeOut());
        // 从连接池中获取连接超时时间
        requestBuilder = requestBuilder.setConnectionRequestTimeout(restfulTemplateProperties.getRequestTimeOut());
        // 读取超时时间
        requestBuilder = requestBuilder.setSocketTimeout(restfulTemplateProperties.getSockedTimeOut());
        // 初始化httpClient
        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestBuilder.build())
                .setConnectionManager(pollingConnectionManager).build();
        // 连接工厂
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        // 配置请求拦截器
        if (!CollectionUtils.isEmpty(interceptors)) {
            restTemplate.setInterceptors(interceptors);
        }

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(fastJsonHttpMessageConverter4 == null ? new FastJsonHttpMessageConverter4()
                : fastJsonHttpMessageConverter4);
        fastjsonConverterRestTemplate = new RestTemplate(messageConverters);
        fastjsonConverterRestTemplate.setRequestFactory(clientHttpRequestFactory);
        // 配置请求拦截器
        if (!CollectionUtils.isEmpty(fastjsonInterceptors)) {
            fastjsonConverterRestTemplate.setInterceptors(fastjsonInterceptors);
        }

    }

    /**
     * get调用
     *
     * @param url           url
     * @param typeReference 返回参数泛型类型
     * @param urlVariables  url参数
     * @return
     * @author 高佳
     * @date 2015年7月6日
     * @update
     */
    public <T> T getForObject(String url, TypeReference<T> typeReference, Object... urlVariables) {
        HttpEntity<String> entity = new HttpEntity<String>(getHttpHeaders());
        String json = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * @param url
     * @param httpHeaders
     * @param typeReference
     * @param urlVariables
     * @param <T>
     * @return
     * @author 刘德云
     * @date 2017年11月21日20:47:08
     */
    public <T> T getForObject(String url, HttpHeaders httpHeaders, TypeReference<T> typeReference, Object... urlVariables) {
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
        String json = restTemplate.exchange(url, HttpMethod.GET, entity,
                String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * POST 请求
     *
     * @param url           url
     * @param request       请求参数对象
     * @param typeReference 返回Object 类型
     * @param urlVariables  url参数
     * @return
     * @author 高佳
     * @date 2015年7月6日
     * @update
     */
    public <T> T postForObject(String url, Object request, TypeReference<T> typeReference, Object... urlVariables) {
        String requestJson = JSON.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, getHttpHeaders());
        String json = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * @param url
     * @param request
     * @param httpHeaders
     * @param typeReference
     * @param urlVariables
     * @param <T>
     * @return
     * @author 刘德云
     * @date 2017年11月21日20:50:24
     */
    public <T> T postForObject(String url, Object request, HttpHeaders httpHeaders, TypeReference<T> typeReference,
                               Object... urlVariables) {
        String requestJson = JSON.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,
                httpHeaders);
        String json = restTemplate.exchange(url, HttpMethod.POST, entity,
                String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * POST 请求
     *
     * @param url           url
     * @param request       请求参数对象
     * @param typeReference 返回Object 类型
     * @param urlVariables  url参数
     * @return
     * @author 高佳
     * @date 2015年7月6日
     * @update
     */
    public <T> T exchangeForObject(HttpMethod httpMethod, String url, Object request, TypeReference<T> typeReference,
                                   Object... urlVariables) {
        String requestJson = JSON.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, getHttpHeaders());
        String json = restTemplate.exchange(url, httpMethod, entity, String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * @param url
     * @param request
     * @param httpHeaders
     * @param typeReference
     * @param urlVariables
     * @param <T>
     * @return
     * @author 刘德云
     * @date 2017年11月21日20:50:24
     */
    public <T> T exchangeForObject(HttpMethod httpMethod, String url, Object request, HttpHeaders httpHeaders,
                                   TypeReference<T> typeReference, Object... urlVariables) {
        String requestJson = JSON.toJSONString(request);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,
                httpHeaders);
        String json = restTemplate.exchange(url, httpMethod, entity, String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * 使用fastjson做转换器的RestTemplate发送请求
     * @param httpMethod
     * @param url
     * @param request
     * @param httpHeaders
     * @param typeReference
     * @param urlVariables
     * @param <T>
     * @return
     */
    public <T> T fastjsonExchangeForObject(HttpMethod httpMethod, String url, Object request, HttpHeaders httpHeaders,
                                   TypeReference<T> typeReference, Object... urlVariables) {
        HttpEntity entity = new HttpEntity(request, httpHeaders);
        String json = fastjsonConverterRestTemplate.exchange(url, httpMethod, entity, String.class, urlVariables).getBody();
        return JSON.parseObject(json, typeReference);
    }

    /**
     * 设置通用的头信息
     * @return
     * @author 陈宇霖
     * @date 2017年11月22日15:10:42
     */
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Accept", "application/json");
        return headers;
    }

    public RestfulTemplateProperties getRestfulTemplateProperties() {
        return restfulTemplateProperties;
    }

    public void setRestfulTemplateProperties(RestfulTemplateProperties restfulTemplateProperties) {
        this.restfulTemplateProperties = restfulTemplateProperties;
    }

    public List<ClientHttpRequestInterceptor> getInterceptors() {
        return interceptors;
    }

    public List<ClientHttpRequestInterceptor> getFastjsonInterceptors() {
        return fastjsonInterceptors;
    }

    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        this.interceptors = interceptors;
        //如果在已经构建了restTemplate对象之后再来添加，需要重新设置template中的拦截器
        if (this.restTemplate != null) {
            restTemplate.setInterceptors(interceptors);
        }
    }

    public void addInterceptor(ClientHttpRequestInterceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>(1);
        }
        interceptors.add(interceptor);
        //如果在已经构建了restTemplate对象之后再来添加，需要重新设置template中的拦截器
        if (this.restTemplate != null) {
            restTemplate.setInterceptors(interceptors);
        }
    }

    public void setFastjsonInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        this.fastjsonInterceptors = interceptors;
        //如果在已经构建了restTemplate对象之后再来添加，需要重新设置template中的拦截器
        if (this.fastjsonConverterRestTemplate != null) {
            fastjsonConverterRestTemplate.setInterceptors(interceptors);
        }
    }

    public void addFastjsonInterceptor(ClientHttpRequestInterceptor interceptor) {
        if (fastjsonInterceptors == null) {
            fastjsonInterceptors = new ArrayList<>(1);
        }
        fastjsonInterceptors.add(interceptor);
        //如果在已经构建了restTemplate对象之后再来添加，需要重新设置template中的拦截器
        if (this.fastjsonConverterRestTemplate != null) {
            fastjsonConverterRestTemplate.setInterceptors(fastjsonInterceptors);
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public FastJsonHttpMessageConverter4 getFastJsonHttpMessageConverter4() {
        return fastJsonHttpMessageConverter4;
    }

    public void setFastJsonHttpMessageConverter4(FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4) {
        this.fastJsonHttpMessageConverter4 = fastJsonHttpMessageConverter4;
    }
}
