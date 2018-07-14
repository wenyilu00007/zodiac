package com.wyl.zodiac.springboot.autoconfig.cas;

import com.wyl.zodiac.core.constant.UrlConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
* @Title: CasClientProperties
* @Package com.wyl.leo.config
* @Description: Cas-Client Params Config
* @author
* @date 2017/8/1 18:12
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.cas.client")
public class CasClientProperties {

    /**
     * CAS Server URL e.g. https://cas.wyl.net/cas
     */
    private String casServerUrlPrefix;

    /**
     * CAS Server Login URL e.g. https://cas.wyl.net/cas/login
     */
    private String casServerLoginUrl;

    /**
     * CAS protected App name e.g. https://app.wyl.net/leo
     */
    private String appServerName;

    /**
     * CAS Client Authentication Filter Url Pattern '/*' as default
     */
    private List<String> authenticationUrlPatterns = Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN);

    /**
     * CAS Client Authentication Filter Exclusions
     */
    private String authenticationExclusions;

    /**
     * CAS Client Validation Filter Url Pattern '/*' as default
     */
    private List<String> validationUrlPatterns = Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN);

    /**
     * CAS Client Validation Filter Exclusions
     */
    private String validationExclusions;

    /**
     * CAS Client Request Wrapper Filter Url Pattern '/*' as default
     */
    private List<String> requestWrapperUrlPatterns = Arrays.asList(UrlConstants.MATCH_ALL_URL_PATTERN);

    /**
     * The Redirect URL when validate fail e.g https://app.wyl.net/index
     */
    private String redirectServerUrl;

    /**
     * CAS Server Logout URL e.g. https://cas.wyl.net/cas/logout
     */
    private String casServerLogoutUrl;

    /**
     * CAS Client Authentication Fail Redirect Strategy Class
     * e.g. com.wyl.leo.config.cas.DefaultAuthenticationRedirectStrategy
     */
    private String authenticationRedirectStrategyClass;

    /**
     * font main page url, use for backend redirect after login success
     */
    private String fontMainPageUrl;

    /**
     * Specify whether the filter should redirect the user agent after a
     * successful validation to remove the ticket parameter from the query
     * string.
     */
    private boolean redirectAfterValidation = true;

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public String getAppServerName() {
        return appServerName;
    }

    public void setAppServerName(String appServerName) {
        this.appServerName = appServerName;
    }

    public List<String> getAuthenticationUrlPatterns() {
        return authenticationUrlPatterns;
    }

    public void setAuthenticationUrlPatterns(List<String> authenticationUrlPatterns) {
        this.authenticationUrlPatterns = authenticationUrlPatterns;
    }

    public List<String> getValidationUrlPatterns() {
        return validationUrlPatterns;
    }

    public void setValidationUrlPatterns(List<String> validationUrlPatterns) {
        this.validationUrlPatterns = validationUrlPatterns;
    }

    public List<String> getRequestWrapperUrlPatterns() {
        return requestWrapperUrlPatterns;
    }

    public void setRequestWrapperUrlPatterns(List<String> requestWrapperUrlPatterns) {
        this.requestWrapperUrlPatterns = requestWrapperUrlPatterns;
    }

    public String getRedirectServerUrl() {
        return redirectServerUrl;
    }

    public void setRedirectServerUrl(String redirectServerUrl) {
        this.redirectServerUrl = redirectServerUrl;
    }

    public String getCasServerLogoutUrl() {
        return casServerLogoutUrl;
    }

    public void setCasServerLogoutUrl(String casServerLogoutUrl) {
        this.casServerLogoutUrl = casServerLogoutUrl;
    }

    public String getAuthenticationRedirectStrategyClass() {
        return authenticationRedirectStrategyClass;
    }

    public void setAuthenticationRedirectStrategyClass(String authenticationRedirectStrategyClass) {
        this.authenticationRedirectStrategyClass = authenticationRedirectStrategyClass;
    }

    public String getAuthenticationExclusions() {
        return authenticationExclusions;
    }

    public void setAuthenticationExclusions(String authenticationExclusions) {
        this.authenticationExclusions = authenticationExclusions;
    }

    public String getValidationExclusions() {
        return validationExclusions;
    }

    public void setValidationExclusions(String validationExclusions) {
        this.validationExclusions = validationExclusions;
    }

    public String getFontMainPageUrl() {
        return fontMainPageUrl;
    }

    public void setFontMainPageUrl(String fontMainPageUrl) {
        this.fontMainPageUrl = fontMainPageUrl;
    }

    public boolean isRedirectAfterValidation() {
        return redirectAfterValidation;
    }

    public void setRedirectAfterValidation(boolean redirectAfterValidation) {
        this.redirectAfterValidation = redirectAfterValidation;
    }
}
