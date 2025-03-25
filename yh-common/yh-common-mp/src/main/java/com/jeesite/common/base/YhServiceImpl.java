package com.jeesite.common.base;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeesite.common.annotation.JhyjDeleted;
import com.jeesite.common.utils.MybatisPlusUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class YhServiceImpl<M extends YhBaseMapper<T>, T> extends ServiceImpl<M, T> implements YhService<T> {

    @Value("${default.page}")
    private Integer defPage;

    @Value("${default.size}")
    private Integer defSize;

    @Override
    public Long queryCount(T t) {
        return count(MybatisPlusUtils.getQueryWrapper(t, null));
    }

    @Override
    public List<T> queryList(T t) {
        QueryWrapper<T> queryWrapper = MybatisPlusUtils.getQueryWrapper(t, null);
        return list(queryWrapper);
    }

    @Override
    public List<T> queryFieldsList(T t, String fields) {
        try {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
            QueryWrapper<T> readWrapper = MybatisPlusUtils.getReadWrapper(fields, entityClass.newInstance());
            if(readWrapper == null){
                return null;
            }
            QueryWrapper<T> queryWrapper = MybatisPlusUtils.getQueryWrapper(t, null);
            return baseMapper.queryFieldsList(readWrapper, queryWrapper, tableInfo.getTableName());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IPage<T> queryPage(T t, Integer page, Integer size) {
        if (page == null) {
            page = defPage;
        }
        if (size == null) {
            size = defSize;
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        QueryWrapper<T> queryWrapper = MybatisPlusUtils.getQueryWrapper(t, null);
        Long total =  count(queryWrapper);
        List<T> list = new ArrayList<>();
        if(total > 0){
            list = baseMapper.page(queryWrapper, (page - 1) * size, page * size, tableInfo.getTableName());
        }
        IPage<T> iPage = new Page<>();
        iPage.setSize(size);
        iPage.setCurrent(page);
        iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
        iPage.setTotal(total);
        iPage.setRecords(list);
        return iPage;
    }

    @Override
    public T infoById(Serializable id) {
        QueryWrapper<T> queryWrapper = new QueryWrapper();
        // 获取待修改对象所有字段
        Field[] fields = ReflectUtil.getFields(entityClass);
        // 遍历每一个字段
        Field idField = null;
        for(Field field : fields) {
            // 查看该字段是否含有JhyjField标签，如果没有标签过滤
            TableId tableId = field.getDeclaredAnnotation(TableId.class);
            if(null != tableId){
                idField = field;
            }
        }
        queryWrapper.eq(idField.getName(), id);
        return getOne(queryWrapper);
    }

    @Override
    public Boolean deleteLogicById(Serializable id) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        // 获取待修改对象所有字段
        Field[] fields = ReflectUtil.getFields(entityClass);
        // 遍历每一个字段
        Field delField = null;
        Field idField = null;
        for(Field field : fields) {
            // 查看该字段是否含有JhyjField标签，如果没有标签过滤
            TableId tableId = field.getDeclaredAnnotation(TableId.class);
            JhyjDeleted jhyjDeleted = field.getDeclaredAnnotation(JhyjDeleted.class);
            if(null != jhyjDeleted){
                delField = field;
            }
            if(null != tableId){
                idField = field;
            }
        }
        updateWrapper.eq(idField.getName(), id);
        updateWrapper.set(delField.getName(), 1);
        return update(updateWrapper);
    }

    @Override
    public Boolean deleteById(Serializable id) {
        return removeById(id);
    }

    @Override
    public Boolean updateOrCleanById(T t) {
        return update(MybatisPlusUtils.getUpdateWrapper(t));
    }
}
