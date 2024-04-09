package com.atguigu.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.vo.cmn.DictEeVo;

import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-02 22:22
 */
public class ExcelListener extends AnalysisEventListener<UserDate> {
    /**
     * 一行一行的读取数据，从第二行开始
     * @param userDate
     * @param analysisContext
     */
    @Override
    public void invoke(UserDate userDate, AnalysisContext analysisContext) {
        System.out.println(userDate);
    }

    /**
     * 读取之后执行
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 读取表头信息
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }
}
