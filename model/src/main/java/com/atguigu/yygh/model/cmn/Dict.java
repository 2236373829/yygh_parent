package com.atguigu.yygh.model.cmn;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Dict
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "数据字典")
@TableName("dict")
public class Dict {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    // 更新时间
    @TableField("update_time")
    private Date updateTime;

    // 逻辑删除(1:已删除，0:未删除)
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    // 其他参数
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();

    // 上级id
    @TableField("parent_id")
    private Long parentId;

    // 名称
    @TableField("name")
    private String name;

    // 值
    @TableField("value")
    private String value;

    // 编码
    @TableField("dict_code")
    private String dictCode;

    // 是否包含子节点
    @TableField(exist = false)
    private boolean hasChildren;

}
