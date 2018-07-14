package com.wyl.zodiac.web.request;

/**
 * @author
 * @version V1.0
 * @title: BaseVO
 * @package com.wyl.leo.common.vo
 * @description vo 基类
 * @date 2017/8/6
 */
public class BaseVO {

    private int page;

    private int limit;

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
