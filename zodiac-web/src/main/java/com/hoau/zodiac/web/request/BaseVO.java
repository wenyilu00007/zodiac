package com.hoau.zodiac.web.request;

/**
 * @author 刘德云
 * @version V1.0
 * @title: BaseVO
 * @package com.hoau.leo.common.vo
 * @description vo 基类
 * @date 2017/8/6
 */
public class BaseVO<T> {

    /**
     * 通用查询参数
     */
    private String query;

    /**
     * 实际值
     */
    private String value;

    /**
     * 显示值
     */
    private String displayName;

    /**
     * 返回的实体对象
     */
    private T result;

    private int page;

    private int limit;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
