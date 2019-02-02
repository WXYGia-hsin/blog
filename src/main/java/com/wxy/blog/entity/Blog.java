package com.wxy.blog.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//用户唯一标识

    @NotEmpty(message = "标题不能为空")
    @Size(min=2,max =50)
    @Column(nullable =false,length = 50)
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2,max =300)
    @Column(nullable =false)
    private String summary;

    @Lob
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable =false)
    private String content;

    @Lob
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable =false)
    private String htmlContent;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp// 由数据库自动创建时间
    private Timestamp createTime;

    @Column(name ="reading")
    private Long reading=0L;//阅读量，访问量



    @Column(name="commentSize")
    private Integer commentSize = 0;  // 评论量

    @Column(name ="likes")
    private Long likes=0L;//点赞量



    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    protected Blog() {
        // TODO Auto-generated constructor stub
    }
    public Blog(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getReading() {
        return reading;
    }

    public void setReading(Long reading) {
        this.reading = reading;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize=this.comments.size();
    }

    /**
     * 添加评论
     * @param comment
     */
    public void addComment(Comment comment){
        this.comments.add(comment);
        this.commentSize=this.comments.size();
    }

    public void removeComment(Long commentId){
        for (int index=0;index<this.comments.size();index++){
            if (comments.get(index).getId() == commentId) {
                this.comments.remove(index);
                break;
            }
        }
        this.commentSize = this.comments.size();
    }
}
