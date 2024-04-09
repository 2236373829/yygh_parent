package com.atguigu.yygh.hosp.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

/**
 * @author xyzZero3
 * @create 2024-03-23 22:51
 */
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // this.setFieldValByName("status", 1, metaObject); // 医院状态 1使用 0不能使用
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
