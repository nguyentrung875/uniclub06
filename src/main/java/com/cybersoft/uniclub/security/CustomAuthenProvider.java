package com.cybersoft.uniclub.security;

import com.cybersoft.uniclub.dto.RoleDTO;
import com.cybersoft.uniclub.exception.AuthenException;
import com.cybersoft.uniclub.request.AuthenRequest;
import com.cybersoft.uniclub.service.AuthenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//Buổi 40: Custom JWT
@Component
public class CustomAuthenProvider implements AuthenticationProvider {

    @Autowired
    private AuthenService authenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        System.out.println("hello authen provider");

        //Lấy username và password do người dùng truyền vào
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();


        AuthenRequest authenRequest = new AuthenRequest(username, password);

        List<RoleDTO> roleDTOList = authenService.checkLogin(authenRequest);

        if (roleDTOList.size() > 0){
            //streamAPI
            //map(): cho phép biển đổi kiểu dữ liệu của biến gốc trong quá trình duyệt mảng/đối tượng
            //Hàm map phải có giá trị trả về

            //Cách dùng java core
//            List<GrantedAuthority> authorityList = new ArrayList<>();
//            roleDTOList.forEach(roleDTO -> {
//                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleDTO.getName());
//                authorityList.add(simpleGrantedAuthority);
//            });

            //Dùng streamAPI
            var authorityList
                    = roleDTOList.stream().map(item -> new SimpleGrantedAuthority(item.getName())).toList();

            //principal là username, credentials là password, vì mình chỉ tạo chứng thực chứ ko lưu lại username, password nên để trống
            return new UsernamePasswordAuthenticationToken("", "", authorityList); //trả về principal và danh sách roll
        } else {
            throw new AuthenException("Đăng nhập thất bại!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //Khai báo loại chứng thực mình hỗ trợ là UsernamePasswordAuthenticationToken
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
