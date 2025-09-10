package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final String SECRET_KEY = "QUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVphYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ejEyMzQ1Njc4OTAxMjM0NTY=";
    private final int EXPIRATION_TIME = 86400000; // 24 hours


    private SecretKey getSigningKey() {
        // 如果没有配置，则生成一个随机密钥（仅用于开发！）
        if (SECRET_KEY == null || SECRET_KEY.trim().isEmpty()) {
            byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
            String generatedKey = Encoders.BASE64.encode(keyBytes);
            System.err.println("⚠️  JWT secret 未配置，使用自动生成密钥（仅限测试）：");
            System.err.println("Generated JWT Secret (Base64): " + generatedKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        // 确保密钥是 Base64 编码
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("JWT secret key 至少需要 256 bits (32 bytes)");
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT secret 必须是合法的 Base64 编码字符串", e);
        }
    }

    /**
     * 生成 JWT Token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // 明确指定算法
                .compact();
    }

    /**
     * 从 Token 中提取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // 解析失败或签名无效
        }
    }

    /**
     * 检查 Token 是否过期
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // 解析失败视为已过期
        }
    }

    /**
     * 获取所有 Claims（封装解析逻辑）
     */
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("无效的 JWT token", e);
        }
    }

    /**
     * 可选：获取 Token 的过期时间（用于前端刷新）
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * 可选：判断 Token 是否快过期（例如还剩 5 分钟）
     */
    public boolean isTokenNearExpiry(String token, long thresholdMillis) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis() <= thresholdMillis;
    }
}
