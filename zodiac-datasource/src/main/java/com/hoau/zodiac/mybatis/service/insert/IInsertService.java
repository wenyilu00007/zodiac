package com.hoau.zodiac.mybatis.service.insert;

/**
 * @author 刘德云
 * @version V1.0
 * @title: IDeleteService
 * @package com.hoau.leo.common.mapper.service.insert
 * @description 保存 service
 * @date 2017/8/6
 */
public interface IInsertService<T> {

    /**
     * 保存记录
     *
     * @param record
     * @return
     */
    int save(T record);

    /**
     * 保存记录
     * 属性为null的不会保存使用数据库默认值
     *
     * @param record
     * @return
     */
    int saveSelective(T record);
}
