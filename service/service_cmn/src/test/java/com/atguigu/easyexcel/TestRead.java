package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.vo.cmn.DictEeVo;

/**
 * @author xyzZero3
 * @create 2024-04-02 22:28
 */
public class TestRead {
    public static void main(String[] args) {
        // 读取文件路径
        String fileName = "D:\\Project\\Java\\atguigu\\yygh_excel\\dict.xlsx";

        // 调用方法实现读取操作
        EasyExcel.read(fileName, UserDate.class, new ExcelListener()).sheet(0).doRead();
    }
}
