package com.itheima.reggie.dto;

import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
}
