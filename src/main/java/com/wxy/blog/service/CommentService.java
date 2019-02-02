package com.wxy.blog.service;

import com.wxy.blog.entity.Comment;

public interface CommentService {
    /**
     *
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     *
     * @param id
     */
    void removeComment(Long id);

}
