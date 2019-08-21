package com.simba.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据操作接口
 */
public interface DataOper {

    ///////////////////////////////////////////  String操作  /////////////////////////////////////////////////////

    String getString(String key);

    void setString(String key, String value);

    void setStringIsNotExist(String key,String value);

    String getRangeValue(String key,int startOfferset,int endOfferset);

    String getAndset(String key,String newValue);

    Long getValueLength(String key);

    Long increaseValue(String key);

    Long increaseValueBy(String key ,int num);

    Long decrease(String key);

    Long decrease(String key,int num);

    Object getObject(String key);

    void setObject(String key, Object value);

    ///////////////////////////////////////////  hash操作  /////////////////////////////////////////////////////

    void setHash(String tableName,String field,String value);

    String getHash(String tableName,String field);

    void setHashWithMap(String key, Map<String,String> hash);

    List<String> getHashMuiltValue(String tableName, String ... field);

    Map<String,String> getHashAllValue(String tableName);

    Set<String> getHashAllFields(String tableName);

    Long getHashFieldCount(String tableName);

    void delHashField(String tableName,String... field);

    Long increaseHashFieldValue(String tableName,String  field,Long num);

    Boolean decreaseHashFieldValue(String tableName,String field);

    ///////////////////////////////////////////  set操作  /////////////////////////////////////////////////////

    Long addSet(String key,String... value);
}
