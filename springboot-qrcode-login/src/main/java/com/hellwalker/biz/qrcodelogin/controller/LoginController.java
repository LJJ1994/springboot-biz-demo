package com.hellwalker.biz.qrcodelogin.controller;

import com.hellwalker.biz.qrcodelogin.model.CommonResult;
import com.hellwalker.biz.qrcodelogin.model.User;
import com.hellwalker.biz.qrcodelogin.service.LoginTokenService;
import com.hellwalker.biz.qrcodelogin.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/api/login")
public class LoginController {
    public static final ConcurrentHashMap<Integer, User> userMap = new ConcurrentHashMap<>();

    static {
        User u1 = new User(1, "zhangsan", "123");
        User u2 = new User(2, "lisi", "123");
        User u3 = new User(3, "wangwu", "123");

        userMap.put(u1.getId(), u1);
        userMap.put(u2.getId(), u2);
        userMap.put(u3.getId(), u3);
    }

    @Autowired
    private LoginTokenService loginTokenService;

    /**
     * 用户名、密码登录（模拟）
     * @param userid
     * @return
     */
    @GetMapping("/uname")
    @ResponseBody
    public CommonResult unameLogin(@RequestParam("userid") Integer userid,
                                   @RequestParam("pass") String pass,
                                   @RequestParam("device") String device) {
        if (userid == null || StringUtils.isEmpty(pass) || StringUtils.isEmpty(device)) {
            return CommonResult.failed("参数不完整");
        }
        if (!userMap.containsKey(userid) || !pass.equalsIgnoreCase(userMap.get(userid).getPassword())) {
            return CommonResult.failed("用户信息不存在");
        }
        String token = UUIDUtils.generateUUID();
        loginTokenService.setLoginInfo(String.valueOf(userid), token, device);
        return CommonResult.success("登录成功");
    }


}
