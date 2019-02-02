package com.wxy.blog.repository;

import com.wxy.blog.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Authority仓库，权限
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
