package com.wyl.zodiac.datasource.mybatis.service.update;

import org.apache.ibatis.annotations.Param;

/**
 * @author
 * @version V1.0
 * @title: IDeleteService
 * @package com.wyl.leo.common.mapper.service.update
 * @description 更新 service
 * @date 2017/8/6
 */
public interface IUpdateService<T> {
    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") T record, @Param("example") Object example);

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);
}
