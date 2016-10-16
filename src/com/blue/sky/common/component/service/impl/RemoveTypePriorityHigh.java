package com.blue.sky.common.component.service.impl;

import com.blue.sky.common.component.entity.CacheObject;
import com.blue.sky.common.component.service.CacheFullRemoveType;

/**
 * Remove type when cache is full.<br/>
 * when cache is full, compare priority of object in cache, if priority is higher remove it first.<br/>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-12-26
 */
public class RemoveTypePriorityHigh<T> implements CacheFullRemoveType<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(CacheObject<T> obj1, CacheObject<T> obj2) {
        return (obj2.getPriority() > obj1.getPriority()) ? 1 : ((obj2.getPriority() == obj1.getPriority()) ? 0 : -1);
    }
}
