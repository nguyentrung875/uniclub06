package com.cybersoft.uniclub.security;

import com.cybersoft.uniclub.filter.CustomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Các class chạy ở tầng cofig chạy trước app
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Autowired
//    private CustomAuthenProvider customAuthenProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenProvider authenProvider) throws Exception {
        //Ở đây ko cần xài autowired vì: đó là Parameter injection
        //Các class này đang ở trên môi trường IoC rồi nên thay vì autowired mình cũng có thể sử dụng như 1 tham số
        //Có 3 loại IoC injection: Autowired, Constructor injection và parameter injection

        //Bắt cuộc Authentication Manager gọi Custome Provider của mình để sử dụng
        System.out.println("hello authen manager");


        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomFilter customFilter) throws Exception {
        return http
                //Tắt dạng tần công tấn công csrf khi sử dụng các dịch vụ giống nhau
                .csrf(csrf -> csrf.disable())
                //Security không cho phép xài session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Nơi quy định đường dẫn được xài hay ko, cần ROLE chứng thức hay không

                .authorizeHttpRequests(request -> {
                    //Cho phép những đường dẫn này đi qua mà ko cần chứng thực
                    request.requestMatchers("/authen").permitAll();
                    //Xài hasRole phải có prefix: ROLE_ADMIN. Dùng hasAuthority thì ko cần prefix
//                    request.requestMatchers("/product").hasRole("ADMIN");
                    request.requestMatchers("/product/**").hasAuthority("ROLE_ADMIN");
                    //Tất cả các link còn lại phải chứng thực
                    request.anyRequest().authenticated();
                })
                //Thêm custom filter vào trước filter
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
