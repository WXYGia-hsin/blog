package com.wxy.blog.controller;

import com.wxy.blog.entity.User;
import com.wxy.blog.service.BlogService;
import com.wxy.blog.service.VoteService;
import com.wxy.blog.util.ConstraintViolationExceptionHandler;
import com.wxy.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

@Controller
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;
    /**
     * 发表评论
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogid){
        try {
            blogService.createVote(blogid);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"点赞成功","null"));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delectVote(@PathVariable("id") Long id, Long blogid){
        boolean isOwner=false;
        User user=voteService.getVoteByid(id).getUser();
        if (SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        &&SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){

        }

        return ResponseEntity.ok().body(new Response(true,"删除成功","null"));
    }
}
