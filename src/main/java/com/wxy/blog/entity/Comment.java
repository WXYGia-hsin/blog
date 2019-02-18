package com.wxy.blog.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增长
    private Long id;

    @NotEmpty(message = "评论内容不能为空")
    @Size(min=2, max=500)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createtime;

    public Comment(User user, String content) {
        this.content = content;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }



}
