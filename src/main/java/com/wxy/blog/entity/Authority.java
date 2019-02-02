package com.wxy.blog.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * GrantedAuthority => 已被授予的权限
 * 角色权限
 */

@Entity
public class Authority implements GrantedAuthority {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//@GeneratedValue注解存在的意义主要就是为一个实体生成一个唯一标识的主键,IDENTITY主键有数据库自动生成
    private Long id;
    @Column(nullable = false)
    private String name;
    @Override
    public String getAuthority() {
        return name;
    }
}
