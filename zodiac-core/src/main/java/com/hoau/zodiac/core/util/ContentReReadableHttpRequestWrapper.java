package com.hoau.zodiac.core.util;

import org.springframework.http.HttpMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
* @Title: ContentReReadableHttpRequestWrapper 
* @Package com.hoau.zodiac.core.util 
* @Description: 请求体可多次读取的HttpRequest请求包装类
* @author 陈宇霖  
* @date 2017/8/22 21:19
* @version V1.0   
*/
public class ContentReReadableHttpRequestWrapper extends HttpServletRequestWrapper {

    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";


    private final ByteArrayOutputStream cachedContent;

    private final Integer contentCacheLimit;

    private ServletInputStream inputStream;

    private BufferedReader reader;


    /**
     * Create a new ContentReReadableHttpRequestWrapper for the given servlet request.
     * @param request the original servlet request
     */
    public ContentReReadableHttpRequestWrapper(HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        this.contentCacheLimit = null;
    }

    /**
     * Create a new ContentReReadableHttpRequestWrapper for the given servlet request.
     * @param request the original servlet request
     * @param contentCacheLimit the maximum number of bytes to cache per request
     * @since 4.3.6
     * @see #handleContentOverflow(int)
     */
    public ContentReReadableHttpRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
        super(request);
        this.cachedContent = new ByteArrayOutputStream(contentCacheLimit);
        this.contentCacheLimit = contentCacheLimit;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ContentReReadableHttpRequestWrapper.ContentReReadableInputStream(getRequest().getInputStream());
        }
        return this.inputStream;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    @Override
    public String getParameter(String name) {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterValues(name);
    }


    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(FORM_CONTENT_TYPE) &&
                HttpMethod.POST.matches(getMethod()));
    }

    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                String requestEncoding = getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
                        String value = valueIterator.next();
                        this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            this.cachedContent.write('=');
                            this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.cachedContent.write('&');
                            }
                        }
                    }
                    if (nameIterator.hasNext()) {
                        this.cachedContent.write('&');
                    }
                }
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
    }

    /**
     * Return the cached request content as a byte array.
     * <p>The returned array will never be larger than the content cache limit.
     * @see #ContentReReadableHttpRequestWrapper(HttpServletRequest, int)
     */
    public byte[] getContentAsByteArray() {
        return this.cachedContent.toByteArray();
    }

    /**
     * Template method for handling a content overflow: specifically, a request
     * body being read that exceeds the specified content cache limit.
     * <p>The default implementation is empty. Subclasses may override this to
     * throw a payload-too-large exception or the like.
     * @param contentCacheLimit the maximum number of bytes to cache per request
     * which has just been exceeded
     * @since 4.3.6
     * @see #ContentReReadableHttpRequestWrapper(HttpServletRequest, int)
     */
    protected void handleContentOverflow(int contentCacheLimit) {
    }

    /**
     * 将读取过的内容重新写入到流内
     * @param content
     * @throws IOException
     * @author 陈宇霖
     */
    public void reWriteContentIntoInputStream(byte[] content) throws IOException {
        ((ContentReReadableHttpRequestWrapper.ContentReReadableInputStream)getInputStream()).setIs(new ByteArrayInputStream(content));
    }

    private class ContentReReadableInputStream extends ServletInputStream {

        private InputStream is;

        private boolean overflow = false;

        public ContentReReadableInputStream(InputStream is) {
            this.is = is;
        }

        public void setIs(InputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int ch = this.is.read();
            if (ch != -1 && !this.overflow) {
                if (contentCacheLimit != null && cachedContent.size() == contentCacheLimit) {
                    this.overflow = true;
                    handleContentOverflow(contentCacheLimit);
                }
                else {
                    cachedContent.write(ch);
                }
            }
            return ch;
        }

        public boolean isFinished() {
            return false;
        }

        public boolean isReady() {
            return false;
        }

        public void setReadListener(ReadListener listener) {

        }
    }

}
