package com.cybersoft.uniclub.service.imp;

import com.cybersoft.uniclub.dto.RoleDTO;
import com.cybersoft.uniclub.entity.RoleEntity;
import com.cybersoft.uniclub.entity.UserEntity;
import com.cybersoft.uniclub.repository.UserRepository;
import com.cybersoft.uniclub.request.AuthenRequest;
import com.cybersoft.uniclub.service.AuthenService;
import com.cybersoft.uniclub.utils.JwtHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthenServiceImp implements AuthenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<RoleDTO> checkLogin(AuthenRequest request) {
        List<RoleDTO> roleDTOList = new ArrayList<>();
        boolean isSuccess = false;

        UserEntity user = userRepository.findUserEntityByEmail(request.email());
        if (user != null && passwordEncoder.matches(request.password(), user.getPassword())) {
            RoleEntity role = user.getRole();
            RoleDTO  roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName()); //bữa sau dùng stream api

            roleDTOList.add(roleDTO);
        }

        return roleDTOList;
    }

}
