package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class setmeal implements Serializable {

    private static final Long serialVersionUID=1L;

    private Long id;

    //菜品分类id
    private Long categoryId;

    //套餐名称
    private String name;

    //套餐价格
    private Double price;

    //状态：0：停用  1：启用
    private Integer status;

    //编码
    private String code;

    //描述信息
    private String description;

    //图片
    private String image;

    //将时间转换为自己要的时间格式向前端发送(具体还是json数据不变)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)  //插入时填充字段
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)  //插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private Long updateUser;

    //是否删除
    private Integer is_deleted;
}
