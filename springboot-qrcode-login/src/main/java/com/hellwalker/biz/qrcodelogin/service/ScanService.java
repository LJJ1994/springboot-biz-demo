package com.hellwalker.biz.qrcodelogin.service;

import com.hellwalker.biz.qrcodelogin.model.CodeStatus;
import com.hellwalker.biz.qrcodelogin.model.CodeVO;
import com.hellwalker.biz.qrcodelogin.util.BizCodeUtil;
import com.hellwalker.biz.qrcodelogin.util.RedisKeyUtil;
import com.hellwalker.common.result.CommonResult;
import com.hellwalker.common.utils.JwtTokenUtil;
import com.hellwalker.common.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ScanService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginTokenService loginTokenService;

    /**
     * 生成二维码uuid
     */
    public CommonResult generateUUID(){
        try{
            String uuid = UUIDUtils.generateUUID();
            redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUIDKey(uuid),
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
     * 查询二维码uuid状态信息
     */
    public CommonResult queryInfoUUID(String uuid) {

        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getScanUUIDKey(uuid));
        if(object==null){
            return new CommonResult("二维码不存在或者已过期", 400);
        }
        return new CommonResult((CodeVO)object);
    }


    /**
     * 扫描登录，返回待确认信息
     */
    public CommonResult scanQrLogin(String uuid, String mobileToken, String device) {
        // 先查询当前移动端是否已登录
        String loginUserId = loginTokenService.getLoginUserId(device, mobileToken);
        if (StringUtils.isEmpty(loginUserId)) {
            return new CommonResult("该设备未登录", 400);
        }

        try {
            Object o = redisTemplate.opsForValue().get(RedisKeyUtil.getScanUUIDKey(uuid));
            if(null == o){
                return new CommonResult("二维码异常，请重新扫描", 400);
            }
            CodeVO codeVO = (CodeVO) o;
            //获取状态
            CodeStatus codeStatus = codeVO.getCodeStatus();
            // 如果已经扫码, 等待确认
            if (codeStatus == CodeStatus.CONFIRMED) {
                return new CommonResult("二维码扫描成功，等待确认", 10000);
            }
            // 如果未使用
            if(codeStatus == CodeStatus.UNUSED){
                // 生成一次性token
                String onceToken = UUIDUtils.generateUUID();
                System.out.println("一次性token: " + onceToken);
                CodeVO confirmingCodeInfo = BizCodeUtil.getConfirmingCodeInfo(onceToken, device);
                redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUIDKey(uuid),
                        confirmingCodeInfo,
                        RedisKeyUtil.getTimeOut(),
                        TimeUnit.MINUTES);

                // 关联用户id和二维码状态信息
                String CONFIRMING_KEY = String.format(RedisKeyUtil.CONFIRMING_KEY, loginUserId);
                redisTemplate.opsForValue().set(CONFIRMING_KEY, confirmingCodeInfo, RedisKeyUtil.getTimeOut(), TimeUnit.MINUTES);

                // 响应一次性token
                return new CommonResult("请确认登录", 200, confirmingCodeInfo);
            }
        } catch (Exception e){
            log.warn("二维码异常{}",e.getMessage());
            return new CommonResult("内部错误", 500);
        }
        return new CommonResult("二维码异常，请重新扫描", 400);
    }

    /**
     * 确认登录，返回用户token以及对应信息
     * @param uuid 二维码uuid
     * @param onceToken 一次性token
     * @param mobileToken  移动端已登录token
     * @param device 设备信息
     * @return
     */
    public CommonResult confirmQrLogin(String uuid, String onceToken, String mobileToken, String device) {
        // 检验是否登录
        String loginUserId = loginTokenService.getLoginUserId(device, mobileToken);
        if (StringUtils.isEmpty(loginUserId)) {
            return new CommonResult("用户未登录", 400);
        }

        try{
            CodeVO codeVO = (CodeVO) redisTemplate.opsForValue().get(RedisKeyUtil.getScanUUIDKey(uuid));
            if(null == codeVO){
                return new CommonResult("二维码已经失效，请重新扫描", 400);
            }
            //获取状态
            CodeStatus codeStatus = codeVO.getCodeStatus();
            // 如果正在确认中,查询用户信息
            if(codeStatus == CodeStatus.CONFIRMING){
                //校验一次性token
                if (!((String) codeVO.getToken()).equalsIgnoreCase(onceToken)) {
                    return CommonResult.failed("一次性token错误");
                }

                // 生成扫描登录成功的token
                String token = JwtTokenUtil.token(loginUserId);

                //redis二维码状态修改，PC可以获取到
                CodeVO confirmedCodeInfo = BizCodeUtil.getConfirmedCodeInfo(token);
                redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUIDKey(uuid),
                        confirmedCodeInfo,
                        RedisKeyUtil.getTimeOut(),
                        TimeUnit.MINUTES);


                return new CommonResult("扫码登陆成功",200, confirmedCodeInfo);
            }
            return new CommonResult("二维码异常，请重新扫描",400);
        } catch (Exception e){
            log.error("确认二维码异常{}", e);
            return new CommonResult("内部错误",500);
        }
    }
}

