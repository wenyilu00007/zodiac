package com.hoau.zodiac.web.response;

import java.io.Serializable;

/**
* @Title: Response
* @Package com.hoau.hbdp.framework.server.web.response 
* @Description: springMvc分页查询统一返回参数
* @author 陈宇霖  
* @date 2017/6/27 08:12
* @version V1.0   
*/
public class PageResponse<T> extends Response<T> implements Serializable {

    private long totalCount;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
