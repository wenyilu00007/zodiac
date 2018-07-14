package com.wyl.zodiac.datasource.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
* @Title: DynamicDatasource 
* @Package com.wyl.zodiac.datasource.dynamic
* @Description: 动态数据源提供者
* @author
* @date 2017/8/8 14:44
* @version V1.0   
*/
public class DynamicDatasource extends AbstractRoutingDataSource{

    /**
     * 从Holder中获取当前操作的数据源类型，交由spring去获取相应的数据源
     * @return
     * @author
     * @date 2017年08月08日14:45:34
     */
    protected Object determineCurrentLookupKey() {
        return ContextDatasourceTypeHolder.getDatasourceType();
    }
}
