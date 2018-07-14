package com.wyl.zodiac.datasource.mybatis.service.select;


import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author
 * @version V1.0
 * @title: IDeleteService
 * @package com.wyl.leo.common.mapper.service.select
 * @description 查询 service
 * @date 2017/8/6
 */
public interface ISelectService<T> {
    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
    T selectOne(T record);

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    List<T> select(T record);

    /**
     * 查询全部结果
     *
     * @return
     */
    List<T> selectAll();

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record
     * @return
     */
    int selectCount(T record);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
    T selectByPrimaryKey(Object key);

    /**
     * 根据Example条件进行查询
     *
     * @param example
     * @return
     */
    List<T> selectByExample(Object example);

    /**
     * 根据Example条件进行查询总数
     *
     * @param example
     * @return
     */
    int selectCountByExample(Object example);

    /**
     * 根据实体属性和RowBounds进行分页查询
     *
     * @param record
     * @param rowBounds
     * @return
     */
    List<T> selectByRowBounds(T record, RowBounds rowBounds);

    /**
     * 根据example条件和RowBounds进行分页查询
     *
     * @param example
     * @param rowBounds
     * @return
     */
    List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds);

}
