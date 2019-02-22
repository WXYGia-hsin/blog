package com.wxy.blog.service;

import com.wxy.blog.entity.Blog;
import com.wxy.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.Lob;

public interface BlogService {
    /**
     *保存博客
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除博客
     */
    void removeBlog(Long id);

    /**
     * 更新博客
     * @param blog
     * @return
     */
    Blog updateBlog(Blog blog);

    /**
     * 根据id查找博客
     * @param id
     */
    Blog getBlogById(Long id);

    /**
     * 根据用户名分页模糊查询 最新
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据用户名分页模糊查询 最热
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleLikeAndSort(User user,String title,Pageable pageable);

    /**
     *阅读量递增
     * @param id
     */
    void readingIncrease(Long id);
    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    Blog createComment(Long blogId, String commentContent);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     * @return
     */
    void removeComment(Long blogId, Long commentId);

    Blog createVote(Long bolgId);

    void removeVote(Long blogId, Long voteId);
}
