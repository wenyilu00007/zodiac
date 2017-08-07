package com.hoau.zodiac.mybatis.service;


import com.hoau.zodiac.mybatis.service.delete.IDeleteService;
import com.hoau.zodiac.mybatis.service.insert.IInsertService;
import com.hoau.zodiac.mybatis.service.select.ISelectService;
import com.hoau.zodiac.mybatis.service.update.IUpdateService;

/**
 * @author 刘德云
 * @version V1.0
 * @title: IDeleteService
 * @package com.hoau.leo.common.mapper.service
 * @description 基础service公共接口
 * @date 2017/8/6
 */
public interface IBaseService<T> extends IDeleteService<T>, IInsertService<T>, IUpdateService<T>, ISelectService<T> {

}
