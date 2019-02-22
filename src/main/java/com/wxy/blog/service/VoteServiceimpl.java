package com.wxy.blog.service;

import com.wxy.blog.entity.Blog;
import com.wxy.blog.entity.Vote;
import com.wxy.blog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteServiceimpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Override
    public Vote getVoteByid(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
