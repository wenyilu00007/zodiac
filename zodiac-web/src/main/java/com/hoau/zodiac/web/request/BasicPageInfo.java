package com.hoau.zodiac.web.request;

import java.io.Serializable;

/**
* @Title: BasicPageInfo 
* @Package com.hoau.hbdp.framework.server.web.request 
* @Description: 使用PageHelper处理分页请求时的基本分页信息
* @author 陈宇霖  
* @date 2017/7/27 08:36
* @version V1.0   
*/
public class BasicPageInfo implements Serializable {

    private static final long serialVersionUID = -5320692157801064310L;
    /**
     * 当前页数
     */
    private int page;

    /**
     * 分页起始
     */
    private int start;

    /**
     * 每页显示条数
     */
    private int limit;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
