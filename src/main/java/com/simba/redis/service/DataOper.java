package com.simba.redis.service;

import java.util.Map;

/**
 * 数据操作接口
 */
public interface DataOper {


    String getString(String key);

    void setString(String key, String value);

    Object getObject(String key);

    void setObject(String key, Object value);

    String getHash(String tableName,String key);

    void setHash(String key, Map<String,String> hash);

}
