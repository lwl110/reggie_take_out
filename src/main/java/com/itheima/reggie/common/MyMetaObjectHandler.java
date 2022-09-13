package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //自动填充时间
        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        //自动填充用户id
        this.strictInsertFill(metaObject,"createUser", Long.class,BaseContext.getCurrentId());
        this.strictInsertFill(metaObject,"updateUser", Long.class,BaseContext.getCurrentId());

        this.strictInsertFill(metaObject,"isDeleted", Integer.class,0);
    }

    /**
     * 更新操作，自动填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //针对updateById方法导致自动填充失效，用setFieldValByName方法
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);

        //自动填充用户id
        this.setFieldValByName("updateUser", BaseContext.getCurrentId(),metaObject);
    }
}
