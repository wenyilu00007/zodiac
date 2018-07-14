package com.wyl.zodiac.web.filter;

import com.wyl.zodiac.core.security.SecurityAccess;
import com.wyl.zodiac.core.util.ContentReReadableHttpRequestWrapper;
import com.wyl.zodiac.core.util.MD5Utils;
import com.wyl.zodiac.core.util.MapUtils;
import com.wyl.zodiac.core.util.date.DateUtils;
import com.wyl.zodiac.core.util.string.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* @Title: ParamsVerifyFilter
* @Package com.wyl.zodiac.web.filter
* @Description: 参数核验过滤器
* @author
* @date 2017/8/21 14:02
* @version V1.0   
*/
public class ParamsVerifyFilter extends OncePerRequestFilter {

    /**
     * 允许的请求延迟时间(秒), 默认3分钟
     */
    private long requestTimeDelaySeconds = 180;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不进行过滤的请求pattern
     */
    private List<String> excludeUrlPatterns = new ArrayList<String>(Arrays.asList("/health"));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getServletPath();
        boolean matched = false;
        for (String pattern : excludeUrlPatterns) {
            matched = antPathMatcher.match(pattern, url);
            if (matched) {
                break;
            }
        }
        return matched;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTimeStr = request.getHeader("requestTime");
        String apiKey = request.getHeader("apiKey");
        String token = request.getHeader("token");
        String httpMethod = request.getMethod();

        //参数校验
        if (StringUtils.isEmpty(requestTimeStr) || StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(token) || StringUtils.isEmpty(httpMethod)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        //判断请求时间是否超时
        Date requestTime = DateUtils.convert(requestTimeStr, DateUtils.DATE_TIME_FORMAT);
        long differSeconds = DateUtils.getSecondDiff(requestTime, new Date());
        if (differSeconds < 0 || differSeconds > requestTimeDelaySeconds) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        //根据apiKey获取secureKey
        if (StringUtils.isEmpty(apiKey)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
        String secureKey = SecurityAccess.getSecureKey(apiKey);
        if (StringUtils.isEmpty(secureKey)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        //包装request，以便可以多次读取其中内容
        HttpServletRequest requestToUse = request;
        if (!(request instanceof ContentReReadableHttpRequestWrapper)) {
            requestToUse = new ContentReReadableHttpRequestWrapper(request);
        }

        //获取请求的内容
        String paramsString;
        if (HttpMethod.GET.toString().equals(httpMethod)) {
            Map params = requestToUse.getParameterMap();

            //去除空值
            params = MapUtils.removeEmpty(params);
            //按照key进行排序
            paramsString = MapUtils.createLinkString(params);
        } else {
            byte[] content = StreamUtils.copyToByteArray(requestToUse.getInputStream());
            paramsString = new String(content);
            ((ContentReReadableHttpRequestWrapper)requestToUse).reWriteContentIntoInputStream(content);
        }

        //校验签名结果是否正确
        boolean verifyResult = MD5Utils.verify(paramsString, token, secureKey, request.getCharacterEncoding());
        if (verifyResult) {
            filterChain.doFilter(requestToUse, response);
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }

    public long getRequestTimeDelaySeconds() {
        return requestTimeDelaySeconds;
    }

    public void setRequestTimeDelaySeconds(long requestTimeDelaySeconds) {
        this.requestTimeDelaySeconds = requestTimeDelaySeconds;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}
