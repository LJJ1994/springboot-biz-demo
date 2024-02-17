package com.hellwalker.biz.qrcodelogin.service;

import com.hellwalker.biz.qrcodelogin.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginTokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录成功，设置登录信息
     * @param userId
     * @param token
     * @param device
     */
    public void setLoginInfo(String userId, String token, String device) {
        String tokenKey = String.format(RedisKeyUtil.LOGIN_TOKEN_KEY, device, token);
        String userIdKey = String.format(RedisKeyUtil.LOGIN_USERID_KEY, device, userId);

        redisTemplate.opsForValue().set(tokenKey, userId, RedisKeyUtil.LOGIN_TOKEN_TIME_OUT, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(userIdKey, token, RedisKeyUtil.LOGIN_TOKEN_TIME_OUT, TimeUnit.MINUTES);

    }

    /**
     * 根据token，获取用户id
     * @param device
     * @param token
     * @return
     */
    public String getLoginUserId(String device, String token) {
        String tokenKey = String.format(RedisKeyUtil.LOGIN_TOKEN_KEY, device, token);
        Object o = redisTemplate.opsForValue().get(tokenKey);
        if (o == null) return "";

        String userId = (String) o;
        return userId;
    }

    /**
     * 根据设备+用户id，获取token
     * @param device
     * @param userId
     * @return
     */
    public String getLoginToken(String device, String userId) {
        String userIdKey = String.format(RedisKeyUtil.LOGIN_USERID_KEY, device, userId);
        Object o = redisTemplate.opsForValue().get(userIdKey);
        if (o == null) {
            return "";
        }
        String token = ((String) o);
        return token;
    }
}
