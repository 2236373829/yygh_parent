package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xyzZero3
 * @create 2024-04-02 20:25
 */
public class TestWrite {
    public static void main(String[] args) {
        // 构建数据list集合
        List<UserDate> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserDate user = new UserDate();
            user.setUserId(i + 1);
            user.setUserName("LXT" + (i + 1));
            list.add(user);
        }

        // 设置excel文件路径和文件名称
        String fileName = "D:\\Project\\Java\\atguigu\\yygh_excel\\dict.xlsx";

        // 调用方法实现写操作
        EasyExcel.write(fileName, UserDate.class).sheet("用户信息").doWrite(list);
    }
}
