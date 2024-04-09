package com.atguigu.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author xyzZero3
 * @create 2024-04-02 20:23
 */
@Data
public class UserDate {

    @ExcelProperty(value = "用户编号", index = 0)
    private Integer userId;

    @ExcelProperty(value = "用户名称", index = 1)
    private String userName;

}
