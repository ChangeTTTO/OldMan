package com.pn.mapper;

import org.springframework.stereotype.Repository;

/**
 * 通用映射
 */
@Repository
public interface CommonMapper {
    /**
     * 对指定表，指定数据，指定列，+1
     * @param table 表名
     * @param id    数据id
     * @param field 字段
     */
    void incrementCount( String table,
                         String id,
                         String field);

    /**
     * 对指定表，指定数据，指定列，-1
     *
     * @param table
     * @param id
     * @param field
     */
    void decrementCount( String table,
                         String id,
                         String field);
}
