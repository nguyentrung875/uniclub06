package com.cybersoft.uniclub.service;

import com.cybersoft.uniclub.dto.RoleDTO;
import com.cybersoft.uniclub.entity.RoleEntity;
import com.cybersoft.uniclub.request.AuthenRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthenService {
    List<RoleDTO> checkLogin(AuthenRequest authenRequest);
}
