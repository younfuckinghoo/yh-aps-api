package com.jeesite.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

public interface YhService<T> extends IService<T> {

    /**
     * 查询数量
     * @param t
     * @return
     */
    Long queryCount(T t);

    /**
     * 全部查询
     * @param t
     * @return
     */
    List<T> queryList(T t);

    /**
     * 查询指定字段
     * @param t
     * @param fields
     * @return
     */
    List<T> queryFieldsList(T t, String fields);

    /**
     * 分页查询
     * @param t
     * @param page
     * @param size
     * @return
     */
    IPage<T> queryPage(T t, Integer page, Integer size);

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    T infoById(Serializable id);

    /**
     * 通过ID删除(真删)
     * @param id
     * @return
     */
    Boolean deleteById(Serializable id);


    /**
     * 通过ID删除(逻辑删除)
     * @param id
     * @return
     */
    Boolean deleteLogicById(Serializable id);

    /**
     * 通过ID 修改或清空字段
     * @param t
     * @return
     */
    Boolean updateOrCleanById(T t);


}
