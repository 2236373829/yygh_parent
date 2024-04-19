package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xyzZero3
 * @create 2024-04-01 12:18
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildDate(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);

        dictList.forEach(dict -> {
            dict.setHasChildren(this.hasChildren(dict.getId()));
        });

        return dictList;
    }

    @Override
    public void exportDict(HttpServletResponse response) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        // dict --> dictEeVo
        List<DictEeVo> dictEeVoList = new ArrayList<>();
        dictList.forEach(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        });

        // 调用方法进行写入操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet(1).doWrite(dictEeVoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(value = "dict", allEntries = true)
    @Override
    public void importDict(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet(1)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        // 如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)) {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            return baseMapper.selectOne(wrapper).getName();
        }

        Long parentId = this.getDictByDictCode(dictCode).getId();

        return baseMapper.selectOne(new QueryWrapper<Dict>()
                .eq("parent_id", parentId)
                .eq("value", value))
                .getName();
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        // 根据dictCode获取id
        final Dict dict = this.getDictByDictCode(dictCode);
        return this.findChildDate(dict.getId());
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 判斷id下面是否有子節點
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Long ChildrenCount = baseMapper.selectCount(wrapper);

        return ChildrenCount > 0;
    }
}
