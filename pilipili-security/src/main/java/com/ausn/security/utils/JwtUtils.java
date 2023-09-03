package com.ausn.security.utils;

import cn.hutool.core.bean.BeanUtil;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.exception.bizException.UnAuthorizedException;
import com.ausn.entity.PUser;
import com.ausn.security.dao.PUserDao;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils
{

    @Autowired
    private PUserDao pUserDao;
    // 设置 token 有效期为 7 天
    private static final long time = 1000 * 60 * 60 * 24 * 7;
    // 定义签名密钥
    private static final String signature = "AusnUniaGitHub31415926qwertyuiopasdfghjklzxcvbnmAusnUniaGitHub31415926qwertyuiopasdfghjklzxcvbnmAusnUniaGitHub31415926qwertyuiopasdfghjklzxcvbnm";

    /**
     * 根据用户信息生成 JWT token。
     *
     */
    public static String EncodeToken(PUser pUser)
    {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //Header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //Payload
                .claim("phoneNumber", pUser.getPhoneNumber())
                .claim("uid", pUser.getUid())
                .setSubject("Authentication")
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString().replace("-", ""))
                //Signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();
        return jwtToken;
    }

    /**
     * 解码 JWT token，获取其中的 Claims 信息。
     *
     * @param token 待解码的 JWT token 字符串
     * @return Claims对象，包含token中的声明信息；如果解析过程发生异常，则返回null。
     */
    public static Claims DecodeToken(String token)
    {
        try
        {
            // 创建一个 JWT 解析器，设置签名密钥
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signature).parseClaimsJws(token);

            // 获取 JWT 中的 Claims 对象
            Claims body = claimsJws.getBody();

            // 返回解析后的 Claims 对象
            return body;
        }
        catch (JwtException e)
        {
            // 处理异常的情况，例如打印错误消息、记录日志或者返回错误响应
            System.out.println("JWT处理异常: " + e.getMessage());

            // 如果发生异常，返回 null
            return null;
        }
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从Token中解析出电话号码和id，然后从Redis或者数据库中拿到具体的User数
     */
    public PUser getUserFromRedis(String token)
    {

        // 解析 token 获取 Claims 数据
        Claims claims = DecodeToken(token);

        if (claims == null || !claims.containsKey("id"))
        {
            log.info("token:"+token);
            throw new UnAuthorizedException();
        }
        String phoneNumber = (String) claims.get("phoneNumber");
        Long id = (Long) claims.get("uid");
        // 从 Claims 中提取用户数据，并创建 User 对象
        // 返回创建的 User 对象

        String key= RedisConstants.LOGIN_PUSER_KEY_PREFIX+token;
        Map<Object,Object> userMap=stringRedisTemplate.opsForHash().entries(key);
        PUser pUser= BeanUtil.fillBeanWithMap(userMap,new PUser(),false);

        return pUser;
    }




}