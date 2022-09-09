package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 套餐菜品关系
 */
@Data
public class SetmealDish implements Serializable {

    private static final Long serialVersionUID=1L;

    private Long id;

    //套餐id
    private Long setmealId;

    //菜品id
    private Long dishId;

    //菜品名称（冗余字段）
    private String name;

    //菜品原价（冗余字段）
    private Double price;

    //份数
    private Integer copies;

    //排序
    private Integer sort;

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
