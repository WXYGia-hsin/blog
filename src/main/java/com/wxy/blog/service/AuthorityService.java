package com.wxy.blog.service;

import com.wxy.blog.entity.Authority;

public interface AuthorityService {
    /**
     * 根据ID查询 Authority
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
