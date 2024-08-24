package com.cybersoft.uniclub.controller;

import com.cybersoft.uniclub.request.AuthenRequest;
import com.cybersoft.uniclub.response.BaseResponse;
import com.cybersoft.uniclub.service.AuthenService;
import com.cybersoft.uniclub.utils.JwtHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("/authen")
@CrossOrigin //Mặc định sẽ cho tất cả domain khác domain backend gọi được endpoint này
public class AuthenController {

    @Autowired
    private AuthenService authenService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private ObjectMapper  objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<?> authen(@Valid @RequestBody AuthenRequest request) throws JsonProcessingException {

//        //Tạo key
//        SecretKey secretKey = Jwts.SIG.HS256.key().build();
//        //Biến key thành chuỗi để lưu trữ
//        String key = Encoders.BASE64.encode(secretKey.getEncoded());

        UsernamePasswordAuthenticationToken authenToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());
        //Kích hoạt authentication manager
       var authentication = authenticationManager.authenticate(authenToken);

        //Đoạn code phía dưới sẽ luôn chạy khi chứng thực thành công
        //Nếu chứng thực null thì security sẽ báo về lỗi

       System.out.println("Kiem tra authentication: " + authentication.toString());
        List<GrantedAuthority> listRole = (List<GrantedAuthority>) authentication.getAuthorities();

        String data = objectMapper.writeValueAsString(listRole);

//        String token = authenService.checkLogin(request) ? jwtHelper.generateToken("hello") : "";

        String token = jwtHelper.generateToken(data);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(token);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}