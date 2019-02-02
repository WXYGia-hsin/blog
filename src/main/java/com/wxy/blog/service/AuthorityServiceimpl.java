package com.wxy.blog.service;

import com.wxy.blog.entity.Authority;
import com.wxy.blog.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceimpl implements AuthorityService{
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
