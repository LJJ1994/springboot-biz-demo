package com.hellwalker.biz.qrcodelogin.service;

import com.hellwalker.biz.qrcodelogin.model.CodeStatus;
import com.hellwalker.biz.qrcodelogin.model.CodeVO;
import com.hellwalker.biz.qrcodelogin.model.CommonResult;
import com.hellwalker.biz.qrcodelogin.util.BizCodeUtil;
import com.hellwalker.biz.qrcodelogin.util.RedisKeyUtil;
import com.hellwalker.biz.qrcodelogin.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ScanService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成uuid
     */
    public CommonResult generateUUID(){
        try{
            String uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUID(uuid),
                    BizCodeUtil.getUnusedCodeInfo(),
                    RedisKeyUtil.getTimeOut(),
                    TimeUnit.MINUTES);
            return new CommonResult(uuid);
        }catch (Exception e){
            log.warn("redis二维码生成异常{}",e.getMessage());
        }

        return new CommonResult("二维码异常，请重新扫描",400);

    }

    /**
     * 查询uuid状态信息
     */
    public CommonResult queryInfoUUID(String uuid) {

        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getScanUUID(uuid));
        if(object==null){
            return new CommonResult("二维码不存在或者已过期", 400);
        }
        return new CommonResult((CodeVO)object);
    }


    /**
     * 扫描登录，返回待确认信息
     */
    public CommonResult scanQrLogin(String uuid, String account) {
        try {
            Object o = redisTemplate.opsForValue().get(RedisKeyUtil.getScanUUID(uuid));
            if(null == o){
                return new CommonResult("二维码异常，请重新扫描", 400);
            }
            CodeVO codeVO = (CodeVO) o;
            //获取状态
            CodeStatus codeStatus = codeVO.getCodeStatus();
            // 如果未使用
            if(codeStatus == CodeStatus.UNUSED){
                redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUID(uuid),
                        BizCodeUtil.getConfirmingCodeInfo(),
                        RedisKeyUtil.getTimeOut(),
                        TimeUnit.MINUTES);
                //业务逻辑

                return new CommonResult("请确认登录", 200, null);
            }
        } catch (Exception e){
            log.warn("二维码异常{}",e.getMessage());
            return new CommonResult("内部错误", 500);
        }
        return new CommonResult("二维码异常，请重新扫描", 400);
    }

    /**
     * 确认登录，返回用户token以及对应信息
     * @param uuid
     * @param userId 用户id
     * @return
     */
    public CommonResult confirmQrLogin(String uuid, String userId) {

        try{
            CodeVO codeVO = (CodeVO) redisTemplate.opsForValue().get(RedisKeyUtil.getScanUUID(uuid));
            if(null == codeVO){
                return new CommonResult("二维码已经失效，请重新扫描", 400);
            }
            //获取状态
            CodeStatus codeStatus = codeVO.getCodeStatus();
            // 如果正在确认中,查询用户信息
            if(codeStatus == CodeStatus.CONFIRMING){
                //业务逻辑

                // 生成token
                String token = TokenUtil.token(userId);

                //redis二维码状态修改，PC可以获取到
                redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUID(uuid),
                        BizCodeUtil.getConfirmedCodeInfo(token),
                        RedisKeyUtil.getTimeOut(),
                        TimeUnit.MINUTES);


                return new CommonResult("登陆成功",200);
            }
            return new CommonResult("二维码异常，请重新扫描",400);
        } catch (Exception e){
            log.error("确认二维码异常{}", e);
            return new CommonResult("内部错误",500);
        }
    }
}

