package com.wxy.blog.controller;

import com.wxy.blog.entity.Blog;
import com.wxy.blog.entity.User;
import com.wxy.blog.service.BlogService;
import com.wxy.blog.service.UserService;
import com.wxy.blog.util.ConstraintViolationExceptionHandler;
import com.wxy.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.jws.WebParam;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 用户主页控制器
 * 利用其addAttribute()方法即可将服务器的值传递到页面中
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BlogService blogService;

    @Value("${file.service.url}")
    private String fileServerUrl;

    /**
     * 用户主页
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String usersapce(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取个人用户设置界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 保存个人设置
     * @param username
     * @param model
     * @return encode方法是对方法加密，而match方法是用来验证密码和加密后密码是否一致的
     */
    @PostMapping
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());
        //判断密码是否变更
        String rawpassword = originalUser.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoderpasssword = passwordEncoder.encode(user.getPassword());
        boolean isMatch = passwordEncoder.matches(rawpassword, encoderpasssword);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取编辑头像界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername("username");//根据username查询用户实体
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        String avatarUrl = user.getAvatar();
        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveUser(originalUser);
        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

    /**
     * 获取博客列表，查询用户博客
     * @param username
     * @param order
     * @param category
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,//排序类型，最新、最热
                                   @RequestParam(value = "category", required = false) Long category,//博客分类
                                   @RequestParam(value = "keyword", required = false) String keyword,//搜索关键字，博客标签，关键字
                                   @RequestParam(value = "async", required = false) boolean async,//是否异步请求页面
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model
    ) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        if (category != null && category > 0) {
            System.out.print("category:" + category);
            System.out.print("selflink:" + "redirect:/u/" + username + "/blogs?category=" + category);
            return "/u";
        } else if (keyword != null && keyword.isEmpty() == false) {
            System.out.print("keyword:" + keyword);
            System.out.print("selflink:" + "redirect:/u/" + username + "/blogs?keyword=" + keyword);
            return "/u";
        }
        System.out.print("order:" + order);
        System.out.print("selflink:" + "redirect:/u/" + username + "/blogs?order=" + order);
        Page<Blog> page = null;
        if (order.equals("hot")) {//最热查询
            Sort sort = new Sort(Sort.Direction.DESC, "reading", "comments", "likes");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
        }
        if (order.equals("new")) {//最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleLike(user, keyword, pageable);
        }
        List<Blog> list = page.getContent();//当前页面的数据列表、
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");
    }

    /**
     * 获取博客展示页面
     * IsAuthenticated 属性是一个布尔值，指示当前用户是否已通过身份验证（已登录）。
     *
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        blogService.readingIncrease(id);
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blogService.getBlogById(id));
        return "/userspace/blog";
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/edit/{id}")//!!!
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id){
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * 获取新增博客的界面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(Model model) {
        model.addAttribute("blog", new Blog(null, null, null));
//        model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器的地址返回客户端
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

//    @GetMapping("/{username}/blogs/{id}")
//    public String listBlogsByOrder(@PathVariable("id") Long id) {
//
//        System.out.print("blogId:" + id);
//        return "/blog";
//    }

    /**
     * 获取编辑博客的界面
     * @param username
     * @param id 博客id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
//        User user=(User) userDetailsService.loadUserByUsername(username);
//        blog.setUser(user);
        try {
            if (blog.getId()!=null) {
                Blog orignalBlog = blogService.getBlogById(blog.getId());
                orignalBlog.setTitle(blog.getTitle());
                orignalBlog.setContent(blog.getContent());
                orignalBlog.setSummary(blog.getSummary());
                blogService.saveBlog(orignalBlog);
            } else {
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }
}