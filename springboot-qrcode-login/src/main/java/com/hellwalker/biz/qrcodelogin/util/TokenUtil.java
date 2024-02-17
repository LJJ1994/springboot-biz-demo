package com.hellwalker.biz.qrcodelogin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token的工具类
 * 使用jwt生成/验证token（jwt JSON Web Token）
 * jwt由三部分组成: 头部（header).载荷（payload).签名（signature)
 * <p>
 * 1.header头部承载两部分信息：
 * {
 *   “type”: “JWT”, 声明类型，这里是jwt
 *   “alg”: “HS256” 声明加密的算法 通常直接使用 HMAC SHA256
 * }
 * 将头部进行base64加密, 构成了jwt第一部分
 * <p>
 * 2.payload载荷就是存放有效信息的地方
 *  (1).标准中注册的声明
 *  (2).公共的声明 （一般不建议存放敏感信息）
 *  (3).私有的声明 （一般不建议存放敏感信息）
 * 将其进行base64加密，得到Jwt的第二部分
 * <p>
 * 3.signature签证信息由三部分组成：
 * (1).header (base64后的)
 * (2).payload (base64后的)
 * (3).secret
 * 需要base64加密后的header和base64加密后的payload连接组成的字符串，
 * 然后通过header中声明的加密方式进行加盐secret组合加密，构成了jwt的第三部分
 */
@Slf4j
public class TokenUtil {
    /**
     * token的失效时间:30天
     */
    private final static long TIME_OUT = 30 * 24 * 60 * 60 *1000L;

    /**
     * token的密钥
     */
    private final static String SECRET = "abc123aa45";

    /**
     * 生成token
     *
     * @return String
     */
    public static String token(String userId) throws UnsupportedEncodingException {
        String token = null;
        try {
            Date date = new Date(System.currentTimeMillis() + TIME_OUT);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String, Object> headers = new HashMap<>();
            headers.put("type", "jwt");
            headers.put("alg", "HS256");
            token = JWT.create()
                    .withClaim("account", userId)
                    .withExpiresAt(date)
                    .withHeader(headers)
                    .sign(algorithm);
        } catch (IllegalArgumentException | JWTCreationException e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * token验证
     *
     * @param token token
     * @return String
     */
    public static boolean verify(String token) throws UnsupportedEncodingException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            // 客户端可以解密 所以一般不建议存放敏感信息
            log.info("account:" + decodedJWT.getClaim("account").asString());
            return true;
        } catch (IllegalArgumentException | JWTVerificationException e) {
            e.printStackTrace();
            return false;
        }

    }
}


