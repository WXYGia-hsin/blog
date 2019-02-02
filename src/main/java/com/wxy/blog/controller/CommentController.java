package com.wxy.blog.controller;

import com.wxy.blog.service.BlogService;
import com.wxy.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;
    @GetMapping
    public String listComment(@RequestParam(value="blogId",required=true) Long blogId, Model model){

        return "";
    }

}
