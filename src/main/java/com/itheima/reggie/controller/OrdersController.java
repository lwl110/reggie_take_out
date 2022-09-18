package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);

        return R.success("下单成功");
    }

    /**
     * 移动端分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        //当前登录用户id
        Long id = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> eq = Wrappers.lambdaQuery(Orders.class)
                .eq(Orders::getUserId,id)
                .orderByDesc(Orders::getOrderTime);

        ordersService.page(ordersPage,eq);

        //对象拷贝:将第一个参数对象，拷贝到第二个参数的对象中，第三个参数表示忽略拷贝的内容
        BeanUtils.copyProperties(ordersPage,ordersDtoPage);

        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(item,ordersDto);

            Long categoryId = item.getUserId();  //分类id

            LambdaQueryWrapper<OrderDetail> orderId = Wrappers.lambdaQuery(OrderDetail.class)
                    .eq(OrderDetail::getOrderId, item.getNumber());
            //根据订单号查询分类对象
            List<OrderDetail> listOrderDetail = orderDetailService.list(orderId);

            ordersDto.setOrderDetails(listOrderDetail);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }

    /**
     * 客户端订单分页查询
     * @param page
     * @param pageSize
     * @param number
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number,String beginTime,String endTime){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        //当前登录用户id
        Long id = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> eq = Wrappers.lambdaQuery(Orders.class)
                .eq(Orders::getUserId,id)
                .like(number != null, Orders::getNumber, number)
                .between(beginTime!=null&&endTime!=null,Orders::getCheckoutTime,beginTime,endTime)
                .orderByDesc(Orders::getOrderTime);

        BeanUtils.copyProperties(ordersPage,ordersDtoPage);

        ordersService.page(ordersPage,eq);

        List<Orders> records = ordersPage.getRecords();

        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();

            //用户id
            Long userId = item.getUserId();

            LambdaQueryWrapper<OrderDetail> eq1 = Wrappers.lambdaQuery(OrderDetail.class)
                    .eq(OrderDetail::getOrderId, userId);

            List<OrderDetail> orderDetails = orderDetailService.list(eq1);
            ordersDto.setOrderDetails(orderDetails);
            BeanUtils.copyProperties(item,ordersDto);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }

    /**
     * 修改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Orders orders){
        //获取当前登录用户id
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> eq = Wrappers.lambdaQuery(Orders.class)
                .eq(currentId != null, Orders::getUserId, currentId)
                .eq(orders.getId() != null, Orders::getNumber, orders.getId());

        ordersService.update(orders,eq);

        return R.success("订单已派送");
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        //获取用户id
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> ordersEq = Wrappers.lambdaQuery(Orders.class)
                .eq(currentId != null, Orders::getUserId, currentId)
                .eq(orders.getId() != null,Orders::getNumber, orders.getId());

        Orders one = ordersService.getOne(ordersEq);

        long orderId = IdWorker.getId(); //自动生成订单号

        Orders ordersCopy = new Orders();
        OrderDetail orderDetail = new OrderDetail();

        if(one != null){
            LambdaQueryWrapper<OrderDetail> orderDetailEq = Wrappers.lambdaQuery(OrderDetail.class)
                    .eq(one.getNumber() != null, OrderDetail::getOrderId, one.getNumber());

            List<OrderDetail> list = orderDetailService.list(orderDetailEq);

            list.stream().forEach((item) -> {
                item.setOrderId(orderId);
                BeanUtils.copyProperties(item,orderDetail,"id");

                orderDetailService.save(orderDetail);
            });

            one.setId(orderId);
            one.setNumber(String.valueOf(orderId));
            one.setStatus(2);
            String[] strings = {"orderTime", "checkoutTime"};
            BeanUtils.copyProperties(one,ordersCopy,strings);
        }

        ordersService.save(ordersCopy);

        return R.success("已成功追加订单");
    }
}


