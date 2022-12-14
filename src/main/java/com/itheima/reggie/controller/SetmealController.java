package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        //构造分页对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> SetmealDtoPage = new Page<>();

        //条件构造器：过滤和排序
        LambdaQueryWrapper<Setmeal> like = Wrappers.lambdaQuery(Setmeal.class)
                .like(StringUtils.isNotEmpty(name),Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime);

        //执行分页查询
        setmealService.page(setmealPage,like);

        //对象拷贝:将第一个参数对象，拷贝到第二个参数的对象中，第三个参数表示忽略拷贝的内容
        BeanUtils.copyProperties(setmealPage,SetmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();  //分类id

            //根据id查询分类对象
            Category byId = categoryService.getById(categoryId);

            if(byId != null){
                String byIdName = byId.getName();
                setmealDto.setCategoryName(byIdName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        SetmealDtoPage.setRecords(list);

        return R.success(SetmealDtoPage);
    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 根据条件查询套餐数据
     * @return
     */
    @GetMapping("list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> eq = Wrappers.lambdaQuery(Setmeal.class)
                .eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId())
                .eq(setmeal.getStatus() != null,Setmeal::getStatus, setmeal.getStatus())
                .orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(eq);

        return R.success(list);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 套餐起售停售功能 status：0 停售 1 起售
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,@RequestParam List<Long> ids){
        ids.stream().forEach((item) -> {
            LambdaUpdateWrapper<Setmeal> set = Wrappers.lambdaUpdate(Setmeal.class)
                    .eq(item != null, Setmeal::getId, item);

            if(status == 0){
                set.ne(Setmeal::getStatus,0).set(Setmeal::getStatus, 0);

                setmealService.update(set);
            }else{
                set.ne(Setmeal::getStatus,1).set(Setmeal::getStatus, 1);

                setmealService.update(set);
            }
        });

        return R.success("菜品已更改为停售");
    }
}
