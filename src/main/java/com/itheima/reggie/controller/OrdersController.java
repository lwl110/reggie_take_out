package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize){
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
                    .eq(OrderDetail::getOrderId, categoryId);
            //根据订单号查询分类对象
            List<OrderDetail> listOrderDetail = orderDetailService.list(orderId);

            ordersDto.setOrderDetails(listOrderDetail);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }
}
