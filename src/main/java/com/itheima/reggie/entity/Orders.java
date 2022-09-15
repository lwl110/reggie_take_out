package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Orders implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    //订单号
    private String number;

    //订单状态 1待付款 2待配送 3已配送 4已完成 5已取消
    private Integer status;

    //下单用户
    private Long userId;

    //地址id
    private Long addressBookId;

    //下单时间
    //将时间转换为自己要的时间格式向前端发送(具体还是json数据不变)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)  //插入时填充字段
    private LocalDateTime orderTime;

    //结账时间
    //将时间转换为自己要的时间格式向前端发送(具体还是json数据不变)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)  //插入时填充字段
    private LocalDateTime checkoutTime;

    //支付方式 1微信 2支付宝
    private Integer payMethod;

    //实收金额
    private BigDecimal amount;

    //备注
    private String remark;

    private String phone;

    private String address;

    private String userName;

    private String consignee;
}
