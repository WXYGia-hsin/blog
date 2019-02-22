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

    @Column(name ="readSize")
    private Integer  readSize=0;//阅读量，访问量



    @Column(name="commentSize")
    private Integer commentSize = 0;  // 评论量

    @Column(name ="likes")
    private Long likes=0L;//点赞量



    @Column(name = "votesize")
    private Integer votesize=0;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;



    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="blog_vote",joinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "vote_id",referencedColumnName = "id"))
   private  List<Vote> votes;

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

    public Integer  getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
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
    public Integer getVotesize() {
        return votesize;
    }

    public void setVotesize(Integer votesize) {
        this.votesize = votesize;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
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

    /**
     * 删除评论
     * @param commentId
     */
    public void removeComment(Long commentId){
        for (int index=0;index<this.comments.size();index++){
            if (comments.get(index).getId() == commentId) {
                this.comments.remove(index);
                break;
            }
        }
        this.commentSize = this.comments.size();
    }

    /**
     * 点赞
     * @return
     */
    public boolean addVote(Vote vote){
        boolean isExist=false;
        for(int index=0;index<votes.size();index++){
            if (this.votes.get(index).getUser().getId()==vote.getUser().getId()){
                isExist=true;
                break;
            }
        }
        if (!isExist){
            this.votes.add(vote);
            this.votesize=this.votes.size();
        }
        return  isExist;
    }
     public void removeVote(Long blogid){
        for (int index=0;index<this.votes.size();index++){
            if (this.votes.get(index).getId()==blogid){
                this.votes.remove(index);
                break;
            }
        }
     }

}
