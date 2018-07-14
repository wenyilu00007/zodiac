package com.wyl.zodiac.web.filter;

import com.wyl.zodiac.core.util.date.DateUtils;
import com.wyl.zodiac.web.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @Title: LogFilter 
* @Package com.wyl.hbdp.framework.server.web.filter
* @Description: Controller请求日志过滤器，不能使用拦截器实现，拦截器不能读取requestBody中参数
* @author
* @date 2017/7/4 10:18
* @version V1.0   
*/
public class LogFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 是否记录请求日志
     */
    private boolean needLogRequest = true;

    /**
     * 是否记录响应日志
     */
    private boolean needLogResponse = true;

    /**
     * 是否记录header
     */
    private boolean needLogHeader = true;

    /**
     * 是否记录参数
     */
    private boolean needLogPayload = true;

    /**
     * 记录的最大payload大小
     */
    private int maxPayloadLength = 2*1024*1024;

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

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date requestDate = new Date();
        boolean isFirstRequest = !isAsyncDispatch(request);

        //包装缓存requestBody信息
        HttpServletRequest requestToUse = request;
        if (isNeedLogPayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }

        //包装缓存responseBody信息
        HttpServletResponse responseToUse = response;
        if (isNeedLogPayload() && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            //记录请求日志
            if (isNeedLogRequest()) {
                logRequest(requestToUse,requestDate);
            }
            //记录响应日志
            if (isNeedLogResponse()) {
                logResponse(responseToUse);
                //把从response中读取过的内容重新放回response，否则客户端获取不到返回的数据
                resetResponse(responseToUse);
            }
        }
    }

    /**
     * 记录请求日志
     * @param request
     * @param requestDate
     * @author
     * @date 2017年07月04日08:21:41
     */
    protected void logRequest(HttpServletRequest request, Date requestDate) throws IOException {
        String payload = isNeedLogPayload() ? getRequestPayload(request) : "";
        logger.info(createRequestMessage(request, payload,requestDate));
    }

    /**
     * 记录响应日志
     * @param response
     * @author
     * @date 2017年07月04日08:22:00
     */
    protected void logResponse(HttpServletResponse response) {
        String payload = isNeedLogPayload() ? getResponsePayload(response) : "";
        logger.info(createResponseMessage(response, payload, new Date()));
    }

    /**
     * 重新将响应参数设置到response中
     * @param response
     * @throws IOException
     * @author
     * @date 2017年07月04日08:22:28
     */
    protected void resetResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.copyBodyToResponse();
        }
    }

    /**
     * 获取请求体中参数
     * @param request
     * @return
     * @author
     * @date 2017年07月04日08:23:18
     */
    protected String getRequestPayload(HttpServletRequest request) throws IOException {
        String payload = "";
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            payload = getPayloadFromBuf(buf, wrapper.getCharacterEncoding());
        }
        return payload;
    }

    /**
     * 获取响应体中参数
     * @param response
     * @return
     * @author
     * @date 2017年07月04日08:23:34
     */
    protected String getResponsePayload(HttpServletResponse response) {
        String payload = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            payload = getPayloadFromBuf(buf, wrapper.getCharacterEncoding());
        }
        return payload;
    }

    /**
     * 创建请求日志实际需要打印的内容
     * @param request
     * @param payload
     * @param requestDate
     * @return
     * @author
     * @date 2017年07月04日08:24:01
     */
    protected String createRequestMessage(HttpServletRequest request, String payload, Date requestDate) {
        StringBuilder msg = new StringBuilder();
        msg.append("Inbound Message\n----------------------------\n");
        msg.append("Address: ").append(request.getRequestURL()).append("\n");
        msg.append("QueryString: ").append(request.getQueryString()).append("\n");
        msg.append("RequestId: ").append(RequestContext.getRequestId()).append("\n");
        msg.append("RequestDate: ").append(DateUtils.convert(requestDate)).append("\n");
        msg.append("Encoding: ").append(request.getCharacterEncoding()).append("\n");
        msg.append("Content-Type: ").append(request.getContentType()).append("\n");
        if (isNeedLogHeader()) {
            msg.append("Headers: ").append(new ServletServerHttpRequest(request).getHeaders()).append("\n");
        }
        if (isNeedLogPayload()) {
            int length = Math.min(payload.length(), getMaxPayloadLength());
            msg.append("Payload: ").append(payload.substring(0, length)).append("\n");
        }
        msg.append("----------------------------------------------");
        return msg.toString();
    }

    /**
     * 创建响应日志实际需要打印的内容
     * @param response
     * @param payload
     * @param responseDate
     * @return
     * @author
     * @date 2017年07月04日08:24:19
     */
    protected String createResponseMessage(HttpServletResponse response, String payload, Date responseDate) {
        StringBuilder msg = new StringBuilder();
        msg.append("Outbound Message\n----------------------------\n");
        msg.append("RequestId: ").append(RequestContext.getRequestId()).append("\n");
        msg.append("ResponseDate: ").append(DateUtils.convert(responseDate)).append("\n");
        msg.append("Encoding: ").append(response.getCharacterEncoding()).append("\n");
        msg.append("Content-Type: ").append(response.getContentType()).append("\n");
        if (isNeedLogHeader()) {
            msg.append("Headers: ").append(new ServletServerHttpResponse(response).getHeaders()).append("\n");
        }
        boolean needLogContentType = true;
        String contentType = response.getContentType();
        //excel文件导出的不需要记录
        if ("application/octet-stream;charset=UTF-8".equals(contentType)) {
            needLogContentType = false;
        }
        if (isNeedLogPayload() && needLogContentType) {
            int length = Math.min(payload.length(), getMaxPayloadLength());
            msg.append("Payload: ").append(payload.substring(0, length)).append("\n");
        }
        msg.append("----------------------------------------------");
        return msg.toString();
    }

    /**
     * 将bytep[]参数转换为字符串用于输出
     * @param buf
     * @param characterEncoding
     * @return
     * @author
     * @date 2017年07月04日08:25:11
     */
    protected String getPayloadFromBuf(byte[] buf, String characterEncoding) {
        String payload = "";
        if (buf.length > 0) {
            int length = Math.min(buf.length, getMaxPayloadLength());
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return payload;
    }

    public boolean isNeedLogRequest() {
        return needLogRequest;
    }

    public void setNeedLogRequest(boolean needLogRequest) {
        this.needLogRequest = needLogRequest;
    }

    public boolean isNeedLogResponse() {
        return needLogResponse;
    }

    public void setNeedLogResponse(boolean needLogResponse) {
        this.needLogResponse = needLogResponse;
    }

    public boolean isNeedLogHeader() {
        return needLogHeader;
    }

    public void setNeedLogHeader(boolean needLogHeader) {
        this.needLogHeader = needLogHeader;
    }

    public boolean isNeedLogPayload() {
        return needLogPayload;
    }

    public void setNeedLogPayload(boolean needLogPayload) {
        this.needLogPayload = needLogPayload;
    }

    public int getMaxPayloadLength() {
        return maxPayloadLength;
    }

    public void setMaxPayloadLength(int maxPayloadLength) {
        this.maxPayloadLength = maxPayloadLength;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}
