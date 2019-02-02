package com.wxy.blog.service;

import com.wxy.blog.entity.Comment;
import com.wxy.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceimpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
