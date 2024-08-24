package com.cybersoft.uniclub.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretJwk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtHelper {

    @Value("${jwts.key}")
    private String strKey;

    private int expiredTime = 24 * 60 * 60 * 1000;

    public String generateToken(String data){
        //Biến key kiểu string đã lưu trữ trước đó thành SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));
        var key = Decoders.BASE64.decode(strKey);
        //Thời gian hết hạn của token
        Date expiredDate = new Date(System.currentTimeMillis() + expiredTime);

        String token = Jwts.builder()
                .issuedAt(new Date())
                .subject(data)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();

        return token;
    }

    //Giải mã token
    public String decodeToken(String token){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(strKey));

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()//trả về thông tin payload và subject mình đã lưu trước đó
                .getSubject();
    }
}
