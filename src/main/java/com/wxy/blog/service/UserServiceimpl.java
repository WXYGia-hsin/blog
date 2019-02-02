package com.wxy.blog.service;

import com.wxy.blog.entity.User;
import com.wxy.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceimpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @javax.transaction.Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @javax.transaction.Transactional
    @Override
    public void removeUser(Long id) {
        userRepository.delete(id);
    }

    @javax.transaction.Transactional
    @Override
    public void removeUsersInBatch(List<User> users) {
        userRepository.deleteInBatch(users);
    }

    @javax.transaction.Transactional
    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        // 模糊查询
        name = "%" + name + "%";
        Page<User> users = userRepository.findByNameLike(name, pageable);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
