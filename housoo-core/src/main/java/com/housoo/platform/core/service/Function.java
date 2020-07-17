package com.housoo.platform.core.service;

/**
 * 描述 Redis数据业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-08-27 09:59:41
 */
public interface Function<E, T> {
    public T callback(E e);
}
