package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JavaMailSender mailSender;

    //获取配置文件的邮件账号
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public String send(User user) {
        //生成随机数
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            //主题
            mailMessage.setSubject("验证码邮件");

            //内容
            mailMessage.setText("您收到的验证码是：" + code);
            System.out.println("您收到的验证码是：" + code);

            //发给谁
            mailMessage.setTo(user.getPhone());

            //你自己的邮箱（可以去配置文件中获取）
            mailMessage.setFrom(from);

            //发送
            mailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return code;
        }
        return code;
    }
}
