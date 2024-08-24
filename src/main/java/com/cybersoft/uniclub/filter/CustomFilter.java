package com.cybersoft.uniclub.filter;

import com.cybersoft.uniclub.dto.AuthorityDTO;
import com.cybersoft.uniclub.utils.JwtHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Lấy giá trị của header có key "Authorization"
        String authorHeader = request.getHeader("Authorization");
        if (authorHeader != null && authorHeader.startsWith("Bearer ")) {
            //Cắt chuỗi từ vị trí thứ 7 (cắt chuỗi con "Bearer " ra khỏi chuỗi)
            String token = authorHeader.substring(7);
            String data = jwtHelper.decodeToken(token);

            System.out.println("Kiem tra token: " + data);

            if (data != null){
//                List<GrantedAuthority> authorityList = objectMapper.reader().forType(GrantedAuthority.class).readValue(data);
                System.out.println("Kiem tra token lan 2: " + data);

                List<AuthorityDTO> authorityDTOS = objectMapper.readValue(data, new TypeReference<List<AuthorityDTO>>() {});

//                List<GrantedAuthority> authorityList1 = new ArrayList<>();
//                authorityDTOS.forEach(dataDTO -> {
//                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(dataDTO.getAuthority());
//                    authorityList1.add(simpleGrantedAuthority);
//                });

                List<SimpleGrantedAuthority> authorityList1 =
                        authorityDTOS.stream().map(item -> new SimpleGrantedAuthority(item.getAuthority())).toList();

                UsernamePasswordAuthenticationToken authenticationToken =
                        //username, password, roles
                        new UsernamePasswordAuthenticationToken("", "", authorityList1);

                //Tạo giấy thông hành cho user
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authenticationToken);
            }
        }
        //Cho phép đi tiếp đến filter tiếp theo
        filterChain.doFilter(request, response);
    }
}
