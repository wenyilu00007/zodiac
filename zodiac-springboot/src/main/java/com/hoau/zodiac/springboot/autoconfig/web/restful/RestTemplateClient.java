package com.hoau.zodiac.springboot.autoconfig.web.restful;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Title: RestTemplateClient
 * @Package com.hoau.hbdp.webservice.components.rest.client
 * @Description: Rest接口调用客户端
 * @author 陈宇霖
 * @date 2017/07/04 15:03:14
 * @version V1.0
 */
public class RestTemplateClient implements InitializingBean {
	/**
	 * Rest 调用
	 */
	private RestTemplate restTemplate;

	/**
	 * 连接工厂相关设置
	 */
	private RestfulTemplateProperties restfulTemplateProperties;

	/**
	 * 拦截器
	 */
	private List<ClientHttpRequestInterceptor> interceptors;

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
	}

	/**
	 * get调用
	 * 
	 * @param url
	 *            url
	 * @param typeReference
	 *            返回参数泛型类型
	 * @param urlVariables
	 *            url参数
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
	 * POST 请求
	 * 
	 * @param url
	 *            url
	 * @param request
	 *            请求参数对象
	 * @param typeReference
	 *            返回Object 类型
	 * @param urlVariables
	 *            url参数
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

	public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
