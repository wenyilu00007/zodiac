package com.hoau.zodiac.mybatis.service.delete;

/**
 * @author 刘德云
 * @version V1.0
 * @title: IDeleteService
 * @package com.hoau.leo.common.mapper.service.delete
 * @description 删除 service
 * @date 2017/8/6
 */
public interface IDeleteService<T> {
    /**
     * 根据主键删除数据
     *
     * @param key 主键
     * @return
     */
    int deleteByPrimaryKey(Object key);

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record
     * @return
     */
    int delete(T record);

    /**
     * 根据Example条件删除数据
     *
     * @param example
     * @return
     */
    int deleteByExample(Object example);
}
